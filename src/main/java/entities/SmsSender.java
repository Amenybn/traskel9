package entities;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {
    private static final String ACCOUNT_SID = "ACCOUNT_SID";
    private static final String AUTH_TOKEN = "AUTH_TOKEN";

    /**
     * Sends an SMS message using Twilio's Messaging API.
     *
     * @param toPhoneNumber The phone number to send the SMS to.
     * @param messageBody The body of the SMS message.
     */
    private void sendSms(String toPhoneNumber, String messageBody) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                new PhoneNumber(toPhoneNumber),  // to
                new PhoneNumber("+12512378308"), // from
                messageBody
        ).create();

        System.out.println("Successfully sent message with SID: " + message.getSid());
    }

    public static void main(String[] args) {
        SmsSender smsSender = new SmsSender();
        smsSender.sendSms("+21651371144", "Bienvenue dans notre platforme Traskel!");
    }
}
