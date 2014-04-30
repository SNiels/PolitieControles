package be.howest.nmct.politiecontroles.data;

/**
 * Created by Niels on 28/04/2014.
 */
public enum Maand {
    JAN(1,"Januari"),
    FEB(2,"Februari"),
    MAR(3,"Maart"),
    APR(4,"April"),
    MEI(5,"Mei"),
    JUN(6,"Juni"),
    JUL(7,"Juli"),
    AUG(8,"Augustus"),
    SEP(9,"September"),
    OKT(10,"Oktober"),
    NOV(11,"November"),
    DEC(12,"December");
    public final int Number;
    public final String Name;

    Maand(int number, String name){
        Number=number;
        Name=name;
    }

    public static Maand getMaand(int number)
    {
        for(Maand maand:Maand.values()){
            if(maand.Number==number)return maand;
        }
        return null;
    }

    @Override
    public String toString() {
        return Name;
    }
}
