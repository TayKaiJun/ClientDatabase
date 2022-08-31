package sample.Model;

/**
 * Created by Kai Jun on 13/4/2017.
 */
public class Payment {
    //private int clientid;
    //private int paymentid;
    //private int referrerid;

    private String name;
    private String referrername;
    private String insurername;

    private double amount;
    //private int rpaymenttype;
    private String rpaymentdate;
    //private int myptype;
    private String mypdate;
    //private int clientptype;
    private String clientpdate;
    private double commission;

    private boolean clientloan;
    private boolean referrerloan;
    private String referrertransactiontype = "Out";
    private String commtransactiontype = "In";
    private String clienttransactiontype;
    private String mypaymenttransactiontype = "Out";


    public Payment(String name, String referrername, double amount, String rpaymentdate, String mypdate, String clientpdate){
        this.name = name;
        this.referrername = referrername;

        this.amount = amount;
        //this.rpaymenttype = rpaymenttype;
        this.rpaymentdate = rpaymentdate;
        //this.myptype = myptype;
        this.mypdate = mypdate;
        //this.clientptype = clientptype;
        this.clientpdate = clientpdate;

        commission = amount * 0.15;
        if(mypdate != null && clientpdate == null) {
            clientloan = true;
            clienttransactiontype = "In";
        }
        else if(mypdate == null && clientpdate != null){
            clientloan = false;
            clienttransactiontype = "nil";
        } else if(mypdate != null && clientpdate != null){
            clientloan = false;
            clienttransactiontype = "In";
        }

        if(rpaymentdate == null){
            referrerloan = true;
        } else{
            referrerloan = false;
        }
    }



}
