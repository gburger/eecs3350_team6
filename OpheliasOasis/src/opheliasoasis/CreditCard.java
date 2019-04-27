/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opheliasoasis;

/**
 *
 * @author roanm
 */
public class CreditCard {
    String CardHolder;
    int expMonth;
    int expYear;
    int cardNumber;
    int CSV;
    
    public CreditCard(String CardHolder, int expMonth, int expYear, int cardNumber, int CSV){
        this.CardHolder = CardHolder;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cardNumber = cardNumber;
        this.CSV = CSV;
        
    }
    
    public String getCardHolder(){
        return CardHolder;
    }
    
    public int getExpMonth(){
        return expMonth;
    }
    
    public int getExpYear(){
        return expYear;
    }
    
    public int getCardNumber(){
        return cardNumber;
    }
    
    public int getCSV(){
        return CSV;
    }
    public void setCardHolder(String NewName){
        CardHolder = NewName;
    }
    public void setExpMonth(int NewMonth){
        expMonth = NewMonth;
    }
    public void setExpYear(int NewYear){
        expYear = NewYear;
    }
    public void setCardNumber(int NewCardNumber){
        cardNumber = NewCardNumber;
    }
    public void setCSV(int NewCSV){
        CSV = NewCSV;
    }
}
