package dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.testharness;

import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
public class TransactionRunner {

    private final PlatformTransactionManager transactionManager;

    public void executeInTransaction(Runnable runnable) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            runnable.run();
            return null;
        });
    }

    public <T> T executeInTransactionAndReturn(Supplier<T> supplier) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        return transactionTemplate.execute(status -> supplier.get());
    }

}
