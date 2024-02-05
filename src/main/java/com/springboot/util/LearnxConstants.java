package com.springboot.util;

public enum LearnxConstants {

	// As per rohans samsung, project understanding below enum constants can be
	// declared as
	// public static final String EMPLOYEE_ID_MUST = 'employee.id.must';
	EMPLOYEE_NOT_FOUND("employee.not.found"), GENERAL_EXCEPTION_MESSAGE("general.exception"),
	BAD_REQUEST("bad.request"), ALREADY_PRESENT("already.exist");
//	EMPLOYEE_ID_MUST("employee.id.must"),
//	EMPLOYEE_EMAIL_MUST("employee.email.must"), EMPLOYEE_EMAIL_INVALID("employee.email.invalid"),
//	EMPLOYEE_NAME_MUST("employee.name.must"), EMPLOYEE_PASSWORD_MUST("employee.password.must"),
//	EMPLOYEE_PASSWORD_INVALID("employee.password.invalid"), EMPLOYEE_ROLE_MUST("employee.role.must"),
//	EMPLOYEE_JOB_LEVEL_MUST("employee.job.level.must"), EMPLOYEE_UNIT_MUST("employee.unit.must"),
//	EMPLOYEE_OFFICE_MUST("employee.office.must"), EMPLOYEE_ACCESS_MUST("employee.access.must");

	private final String type;

	private LearnxConstants(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}

}
