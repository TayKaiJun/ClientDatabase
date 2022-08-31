package sample.AccountsPage;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Kai Jun on 16/4/2017.
 */
public class Transactions {
    private SimpleStringProperty payer;
    private SimpleStringProperty receiver;
    private SimpleDoubleProperty amount;
    private SimpleStringProperty date;
    private SimpleStringProperty trantype;
    private SimpleStringProperty client;

    public Transactions(){
        this.payer = new SimpleStringProperty();
        this.receiver = new SimpleStringProperty();
        this.amount = new SimpleDoubleProperty();
        this.date = new SimpleStringProperty();
        this.trantype = new SimpleStringProperty();
        this.client = new SimpleStringProperty();
    }

    public Transactions(String payer, String receiver, Double amount, String date, String trantype , String client) {
        this.payer = new SimpleStringProperty(payer);
        this.receiver = new SimpleStringProperty(receiver);
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleStringProperty(date);
        this.trantype = new SimpleStringProperty(trantype);
        this.client = new SimpleStringProperty(client);
    }


    public String getPayer() {
        return payer.get();
    }

    public void setPayer(String payer) {
        this.payer.set(payer);
    }

    public String getReceiver() {
        return receiver.get();
    }

    public void setReceiver(String receiver) {
        this.receiver.set(receiver);
    }

    public double getAmount() {
        return amount.get();
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getTrantype() {
        return trantype.get();
    }


    public void setTrantype(String trantype) {
        this.trantype.set(trantype);
    }

    public String getClient() {
        return client.get();
    }


    public void setClient(String client) {
        this.client.set(client);
    }
}
