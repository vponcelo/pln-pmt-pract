/**
 * 
 */
package xprolog;

/**
 * @author bgutmann
 * 
 */
// /////////////////////////////////////////////////////////////////////
final class Primitive extends TermList
// /////////////////////////////////////////////////////////////////////
{
    int ID = 0;

    public Primitive(String n) {
        try {
            ID = Integer.parseInt(n);
        } catch (Exception e) {
        }
    }

    public String toString() {
        return " <" + ID + "> ";
    }

}
