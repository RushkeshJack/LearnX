package com.springboot.util;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class loggingAspect {

	private static final Logger logger = LoggerFactory.getLogger(loggingAspect.class);

//	@Pointcut("")
//	public void deleteRetailerDetailsPointcut() {
//	}

//	@After("execution(* com.springboot.LearnX.Controller.ProjectController.adminSearchEmployeeGiveAccess(..))")
//	public void logAfterReturningDeleteRetailerDetails() {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String formattedDate = sdf.format(new Date());
//		System.out.println("in advice");
//		logger.info(formattedDate);
//	}
}
