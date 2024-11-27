package it.unibo.exceptions;

import it.unibo.exceptions.fakenetwork.api.NetworkComponent;
import it.unibo.exceptions.fakenetwork.impl.ServiceBehindUnstableNetwork;

import java.io.IOException;
import java.io.PrintStream;

import static it.unibo.exceptions.arithmetic.ArithmeticService.DIVIDED;
import static it.unibo.exceptions.arithmetic.ArithmeticService.MINUS;
import static it.unibo.exceptions.arithmetic.ArithmeticService.PLUS;
import static it.unibo.exceptions.arithmetic.ArithmeticService.TIMES;
import static java.lang.Double.parseDouble;

/**
 * Tests the service.
 */
public final class UseArithmeticService {

    private static final PrintStream LOG = System.out;

    private UseArithmeticService() { }

    /**
     *
     * @param args unused
     */
    public static void main(final String[] args) {
        try {
            new ServiceBehindUnstableNetwork(1);
            throw new AssertionError("Expected an IllegalArgumentException, but no Exception was thrown");
        } catch (final IllegalArgumentException e) {
            LOG.println("Correct: a service with 100% failures cannot be created.");
        }
        final NetworkComponent server = new ServiceBehindUnstableNetwork();
        assertComputeResult(server, "1", "1");
        assertComputeResult(server, "2", "1", PLUS, "1");
        assertComputeResult(server, "9", "1", PLUS, "2", TIMES, "4.0");
        assertComputeResult(server, "0", "1", PLUS, "2", DIVIDED, "4.0", MINUS, "1.5");
        assertThrowsException(server, IllegalArgumentException.class, "1", "power", "2");
        assertThrowsException(server, IllegalArgumentException.class, "1", TIMES, "NaN");
        assertThrowsException(server, IllegalStateException.class, "1", TIMES, PLUS);
        assertThrowsException(server, IllegalStateException.class, "1", TIMES, PLUS, "2");
    }

    private static void retrySendOnNetworkError(final NetworkComponent server, final String message) {
        /*
         * This method should re-try to send message to the provided server, catching all IOExceptions,
         * until it succeeds.
         */
        while(true){
            
            try{
                server.sendData(message);
                return;
            }
            catch(IOException e){
                LOG.println(e.getMessage());
            }
        }
    }

    private static String retryReceiveOnNetworkError(final NetworkComponent server) {
        /*
         * This method should re-try to retrieve information from the provided server, catching all IOExceptions,
         * until it succeeds.
         */

         while(true){

            try{
                server.receiveResponse();
            }
            catch(IOException e){
                LOG.println(e.getMessage());
            }
         }
    }

    private static void assertEqualsAsDouble(final String expected, final String actual) {
        final var message = ": expected " + expected + " and received " + actual;
        /*
         * Never call equality on doubles. If you need exact equality, then you can rely on the compare method,
         * which internally uses the ULPs (Units in the Last Place) to compare the two doubles to support NaNs,
         * negative zeros, and similar corner cases.
         */
        if (Double.compare(parseDouble(expected), parseDouble(actual)) == 0) {
            LOG.println("Success" + message);
        } else {
            throw new AssertionError("ERROR" + message);
        }
    }

    private static void assertComputeResult(
        final NetworkComponent server,
        final String expected,
        final String... operation
    ) {
        for (final var command: operation) {
            retrySendOnNetworkError(server, command);
        }
        assertEqualsAsDouble(expected, retryReceiveOnNetworkError(server));
    }

    private static void assertThrowsException(
        final NetworkComponent server,
        final Class<? extends Throwable> expected,
        final String... operation
    ) {
        try {
            assertComputeResult(server, null, operation);
            throw new AssertionError(expected.getSimpleName() + " expected, but no exception was thrown");
        } catch (final Throwable error) { // NOPMD **never** catch generic exceptions in real life
            assertExceptionIs(expected, error);
        }
    }

    private static void assertExceptionIs(
        final Class<? extends Throwable> expectedException,
        final Throwable actualException
    ) {
        if (!expectedException.isAssignableFrom(actualException.getClass())) {
            throw new AssertionError(
                "Expected exception "
                    + expectedException.getSimpleName()
                    + ", but got "
                    + actualException.getClass().getSimpleName()
            );
        }
        LOG.println(
            "Exception successfully collected: "
                + actualException.getClass().getSimpleName()
                + "["
                + actualException.getMessage()
                + "]"
        );
    }

}
