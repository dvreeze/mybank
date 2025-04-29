package eu.cdevreeze.mybank.web;

import eu.cdevreeze.mybank.context.MyBankApplicationConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class SpringContainerHolder {

    // The idea is to not limit the Spring ApplicationContext to only one servlet using it.
    // Hence, this class. Methods "init" and "destroy" are called by a ServletContextListener.

    // Singleton pattern. Thread-safe. And initialization and destruction can be called successfully only once.

    private final AtomicReference<Optional<GenericApplicationContext>> optionalAppContextReference =
            new AtomicReference<>(Optional.empty());

    private SpringContainerHolder() {
    }

    public void initOnce() {
        optionalAppContextReference.updateAndGet(v -> {
            if (v.isEmpty()) {
                GenericApplicationContext appContext =
                        new AnnotationConfigApplicationContext(MyBankApplicationConfiguration.class);
                appContext.registerShutdownHook();
                return Optional.of(appContext);
            } else {
                return v;
            }
        });
    }

    public void destroyOnce() {
        optionalAppContextReference.getAndUpdate(v -> {
            if (v.isEmpty()) {
                return v;
            } else {
                return Optional.empty();
            }
        });
    }

    public GenericApplicationContext getContainer() {
        return optionalAppContextReference.get().orElseThrow();
    }

    private static final SpringContainerHolder INSTANCE = new SpringContainerHolder();

    public static SpringContainerHolder getInstance() {
        return INSTANCE;
    }
}
