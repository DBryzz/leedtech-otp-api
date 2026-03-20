package org.leedtech.otp.exceptions;

import java.io.Serial;

/**
 *
 * @author DB.Tech
 */
public class LeedTechException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6539904016201632067L;

    private final Problem problem;

    public LeedTechException(Problem problem) {
        this(problem, problem.title());
    }

    public LeedTechException(Problem problem, String message) {
        super(message);
        this.problem = problem;
    }

    public LeedTechException(Problem problem, String message, Throwable cause) {
        super(message, cause);
        this.problem = problem;
    }

    public LeedTechException(Problem problem, Throwable cause) {
        super(cause);
        this.problem = problem;
    }

    protected LeedTechException(Problem problem, String message, Throwable cause,
                                boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.problem = problem;
    }

    public Problem getProblem() {
        return problem;
    }
}
