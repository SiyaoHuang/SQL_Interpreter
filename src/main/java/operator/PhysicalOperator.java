package operator;

import com.sql.interpreter.PhysicalPlanBuilder;
import model.Tuple;
import util.Catalog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Abstract class for operator
 * Created by Yufu Mo
 */
public abstract class PhysicalOperator {

    /**
     * get the next tuple of the operator's output
     * return null if the operator has no more output
     * @return the next tuple of the operator's output
     */
    public abstract Tuple getNextTuple();

    /**
     * reset the operator's state and start returning its output again from the
     * beginning
     */
    public abstract void reset();

    /**
     * for debugging, get all the tuples at once and put them in a file.
     * @param i the index of the output file.
     */
    public void dump(int i) {
        String path = Catalog.getInstance().getOutputPath();
        BufferedWriter output;
        try {
            File file = new File(path + i);
            StringBuilder sb = new StringBuilder();
            output = new BufferedWriter(new FileWriter(file));
            Tuple tuple = getNextTuple();
            while(tuple != null){
                sb.append(tuple.toString());
                sb.append("\n");
                System.out.println(tuple);
                tuple = getNextTuple();
            }
            output.write(sb.toString());
            output.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        reset();
    }

    /**
     * @return the current schema of the operator
     */
    public abstract Map<String, Integer> getSchema();


    /**
     * Abstract method for accepting PhysicalPlanBuilder visitor,
     * in which the visitor would visit the operator
     * @param visitor PhysicalPlanBuilder visitor to be accepted.
     */
    public abstract void accept(PhysicalPlanBuilder visitor);

}
