package Mail;

import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import Flight.FlightInformation;
import Passenger.PassengerInformation;

/*
 * 此类用于将航班和乘客信息打印成表格并且发送邮件。
 * This class is used to print flight and passenger information into forms and send emails.
 */
public class Mail {

    /*
     * 渲染邮件内容。
     * Render the content of the email.
     */
    public static void sendEmail(List<FlightInformation> flights, List<PassengerInformation> passengers) {
        StringBuilder emailBody = new StringBuilder("<html><body><h2>航班信息</h2><table border='1'>");
        emailBody.append("<tr><th>航班号</th><th>出发地</th><th>目的地</th><th>起飞时间</th><th>机型</th><th>可用座位数</th></tr>");

        /*
         * 遍历航班信息，渲染表格。
         * Traverse the flight information and render the table.
         */
        for (FlightInformation flight : flights) {
            emailBody.append("<tr>")
                    .append("<td>").append(flight.getFlightNumber()).append("</td>")
                    .append("<td>").append(flight.getDeparture()).append("</td>")
                    .append("<td>").append(flight.getDestination()).append("</td>")
                    .append("<td>").append(flight.getDepartureTime()).append("</td>")
                    .append("<td>").append(flight.getAircraftType()).append("</td>")
                    .append("<td>").append(flight.getAvailableSeats()).append("</td>")
                    .append("</tr>");
        }
        emailBody.append("</table><h2>乘客信息</h2><table border='1'>");
        emailBody.append("<tr><th>姓名</th><th>性别</th><th>行李重量</th><th>联系方式</th><th>航班号</th></tr>");

        /*
         * 渲染乘客信息。
         * Render passenger information.
         */
        for (PassengerInformation passenger : passengers) {
            emailBody.append("<tr>")
                    .append("<td>").append(passenger.getName()).append("</td>")
                    .append("<td>").append(passenger.getGender()).append("</td>")
                    .append("<td>").append(passenger.getWeightOfLuggage()).append("</td>")
                    .append("<td>").append(passenger.getTelephoneNumber()).append("</td>")
                    .append("<td>").append(passenger.getFlightNumber()).append("</td>")
                    .append("</tr>");
        }

        /*
         * 添加表格结束标签。
         * Add table end tag.
         */
        emailBody.append("</table></body></html>");
        try {
            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

            /*
             * 配置邮箱信息。
             * Config email information.
             */
            Properties props = System.getProperties();

            /*
             * 配置邮件服务器。
             * Configure the email server.
             */
            props.setProperty("mail.smtp.host", "smtp.qq.com");
            props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.smtp.socketFactory.fallback", "false");

            /*
             * 配置邮件服务器端口。
             * Configure the email server port.
             */
            props.setProperty("mail.smtp.port", "465");
            props.setProperty("mail.smtp.socketFactory.port", "465");

            /*
             * 鉴权信息。
             * Authentication information.
             */
            props.setProperty("mail.smtp.auth", "true");

            /*
             * 建立邮件会话。
             * Establish email session.
             */
            Session session = Session.getDefaultInstance(props, new Authenticator() {

                /*
                  身份认证。
                  Authentication.
                 */
                protected PasswordAuthentication getPasswordAuthentication() {

                    /*
                     * 设置账户和授权码。
                     * Set account and authorization code.
                     */
                    return new PasswordAuthentication("2303085802@qq.com", "nefgniwnwhiadhhi");
                }
            });

            /*
             * 建立邮件对象。
             * Establish email object.
             */
            MimeMessage message = new MimeMessage(session);

            /*
             * 设置邮件的发件人。
             * Set the sender's email address.
             */
            message.setFrom(new InternetAddress("2303085802@qq.com"));

            /*
             * 设置邮件的收件人。
             * Set the recipient's email address.
             */
            message.setRecipients(Message.RecipientType.TO, "2303085802@qq.com");

            /*
             * 设置邮件的主题。
             * Set the subject of the email.
             */
            message.setSubject("航班和乘客信息");

            /*
             * 文本部分。
             * Text part.
             */
            message.setContent(emailBody.toString(), "text/html;charset=UTF-8");
            message.saveChanges();

            /*
             * 发送邮件。
             * Send email.
             */
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
/*
 * End of Mail Class.
 * Written and checked by Fan Xinkang.
 */