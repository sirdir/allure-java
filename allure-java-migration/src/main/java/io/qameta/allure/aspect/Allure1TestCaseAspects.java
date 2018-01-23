package io.qameta.allure.aspect;

import io.qameta.allure.Allure;
import io.qameta.allure.Lifecycle;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Objects;

/**
 * Allure junit aspect.
 */
@Aspect
public class Allure1TestCaseAspects {

    private static Lifecycle lifecycle;

    @Before("execution(@org.junit.Test * *.*(..))")
    public void junitTestStart(final JoinPoint joinPoint) {
        updateTestCase(joinPoint);
    }

    @Before("execution(@org.testng.annotations.Test * *.*(..))")
    public void testNgTestStart(final JoinPoint joinPoint) {
        updateTestCase(joinPoint);
    }

    private void updateTestCase(final JoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Object[] args = joinPoint.getArgs();
        final Object target = joinPoint.getTarget();
        final Allure1Annotations annotations = new Allure1Annotations(target, signature, args);

        getLifecycle().updateTest(annotations::updateTitle);
        getLifecycle().updateTest(annotations::updateDescription);
        getLifecycle().updateTest(annotations::updateParameters);
        getLifecycle().updateTest(annotations::updateLabels);
        getLifecycle().updateTest(annotations::updateLinks);
    }

    /**
     * For tests only.
     */
    public static void setLifecycle(final Lifecycle lifecycle) {
        Allure1TestCaseAspects.lifecycle = lifecycle;
    }

    public static Lifecycle getLifecycle() {
        if (Objects.isNull(lifecycle)) {
            lifecycle = Allure.getLifecycle();
        }
        return lifecycle;
    }
}