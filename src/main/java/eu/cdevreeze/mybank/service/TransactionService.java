package eu.cdevreeze.mybank.service;

import eu.cdevreeze.mybank.model.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    private final JdbcTemplate jdbcTemplate;

    private final String bankSlogan;

    public TransactionService(JdbcTemplate jdbcTemplate, @Value("${bank.slogan}") String bankSlogan) {
        this.jdbcTemplate = jdbcTemplate;
        this.bankSlogan = bankSlogan;
    }

    @Transactional
    public Transaction create(BigDecimal amount, Instant timestamp, String reference, String receivingUserId) {
        checkInTransaction();
        String sql = """
                insert into transactions (amount, creation_time, reference, bank_slogan, receiving_user)
                values (?, ?, ?, ?, ?)
                """.strip();

        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator psc = connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setBigDecimal(1, amount);
            ps.setObject(2, timestamp, JDBCType.TIMESTAMP_WITH_TIMEZONE);
            ps.setString(3, reference);
            ps.setString(4, bankSlogan);
            ps.setString(5, receivingUserId);
            return ps;
        };

        jdbcTemplate.update(psc, keyHolder);

        String uuid = !Objects.requireNonNull(keyHolder.getKeys()).isEmpty() ?
                ((UUID) keyHolder.getKeys().values().iterator().next()).toString() :
                null;

        return new Transaction(
                uuid,
                amount,
                timestamp,
                reference,
                bankSlogan,
                receivingUserId
        );
    }

    @Transactional(readOnly = true)
    public Optional<Transaction> findById(String id) {
        checkInReadonlyTransaction();
        String sql = """
                select id, amount, creation_time, reference, bank_slogan, receiving_user
                  from transactions
                 where id = ?
                """.strip();
        PreparedStatementSetter pss = preparedStatement -> {
            preparedStatement.setObject(1, UUID.fromString(id));
        };

        return jdbcTemplate.query(sql, pss, transactionRowMapper()).stream().findFirst();
    }

    @Transactional(readOnly = true)
    public List<Transaction> findByReference(String reference) {
        checkInReadonlyTransaction();
        String sql = """
                select id, amount, creation_time, reference, bank_slogan, receiving_user
                  from transactions
                 where reference = ?
                """.strip();
        PreparedStatementSetter pss = preparedStatement -> {
            preparedStatement.setString(1, reference);
        };

        return jdbcTemplate.query(sql, pss, transactionRowMapper());
    }

    @Transactional(readOnly = true)
    public List<Transaction> findByReceivingUserId(String receivingUserId) {
        checkInReadonlyTransaction();
        String sql = """
                select id, amount, creation_time, reference, bank_slogan, receiving_user
                  from transactions
                 where receiving_user = ?
                """.strip();
        PreparedStatementSetter pss = preparedStatement -> {
            preparedStatement.setString(1, receivingUserId);
        };

        return jdbcTemplate.query(sql, pss, transactionRowMapper());
    }

    @Transactional(readOnly = true)
    public List<Transaction> findAll() {
        checkInReadonlyTransaction();
        String sql = """
                select id, amount, creation_time, reference, bank_slogan, receiving_user
                  from transactions
                """.strip();

        return jdbcTemplate.query(sql, transactionRowMapper());
    }

    private RowMapper<Transaction> transactionRowMapper() {
        return (resultSet, rowNum) ->
                new Transaction(
                        resultSet.getObject("id").toString(),
                        resultSet.getBigDecimal("amount"),
                        resultSet.getObject("creation_time", Instant.class),
                        resultSet.getString("reference"),
                        resultSet.getString("bank_slogan"),
                        resultSet.getString("receiving_user")
                );
    }

    private ResultSetExtractor<Transaction> transactionResultSetExtractor() {
        return resultSet -> transactionRowMapper().mapRow(resultSet, 1);
    }

    private void checkInTransaction() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("Not inside a transaction");
        }
    }

    private void checkInReadonlyTransaction() {
        if (!TransactionSynchronizationManager.isActualTransactionActive() ||
                !TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            throw new IllegalStateException("Not inside a read-only transaction");
        }
    }
}
