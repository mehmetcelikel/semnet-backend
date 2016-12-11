package com.boun.swe.semnet.commons.util;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SemnetLogger {

    /** Handle to the log file */
	private final Logger log = LoggerFactory.getLogger(SemnetLogger.class);

    public SemnetLogger () {}
    @Pointcut("within(com.boun.swe.semnet..*)")
	protected void allMethod() {
	}
    
	@Around("allMethod()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		
		String className = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		String classMethodName = className + "." + methodName;
		
		if(!isDoFilter(classMethodName)){
			log.info(classMethodName + " operation has started");	
		}
		
		long elapsedTime = 0;
		long start = System.nanoTime();
		try {
			
			Object result = joinPoint.proceed();
			
			elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
			
			return result;
		} catch (IllegalArgumentException e) {
			log.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in " + joinPoint.getSignature().getName() + "()");
			
			elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
			
			throw e;
		}finally{
			if(!isDoFilter(classMethodName)){
				log.info(classMethodName + " operation has finished"+Arrays.toString(joinPoint.getArgs())+", elapsedTime->" + elapsedTime + " ms");	
			}
		}
	}
	
	private boolean isDoFilter(String classMethodName){
		return "javax.servlet.Filter.doFilter".equals(classMethodName);
	}
}
