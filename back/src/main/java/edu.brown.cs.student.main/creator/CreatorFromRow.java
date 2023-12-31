package edu.brown.cs.student.main.creator;

import edu.brown.cs.student.main.parser.FactoryFailureException;
import java.util.List;

/**
 * This interface defines a method that allows your CSV parser to convert each row into an object of
 * some arbitrary passed type.
 *<p>
 * Your parser class constructor should take a second parameter of this generic interface type.
 */

// This is an Interface that allows you to use different types of data

public interface CreatorFromRow<T> {

  T create(List<String> row) throws FactoryFailureException;
}