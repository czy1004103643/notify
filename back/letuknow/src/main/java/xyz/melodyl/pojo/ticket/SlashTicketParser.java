package xyz.melodyl.pojo.ticket;

// Base64表中的/(%2F)是不被允许在url中出现的，因此用@(%40)替换
public class SlashTicketParser extends DecoratedTicketParser {

    @Override
    public String encryptContentToTicket(String content) throws Exception {
        return getTicketParser().encryptContentToTicket(content)
                .replace('/', '@')
                .replace("%2F", "%40");
    }

    @Override
    public String decryptTicketToContent(String ticket) throws Exception {
        String decryptStr = ticket.replace('@', '/')
                .replace("%40", "%2F");
        return getTicketParser().decryptTicketToContent(decryptStr);
    }
}
