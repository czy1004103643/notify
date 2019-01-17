package xyz.melodyl.pojo.ticket;

import java.net.URLEncoder;

public class URLTicketParser extends DecoratedTicketParser {

    @Override
    public String encryptContentToTicket(String content) throws Exception{
        String encryptStr = getTicketParser().encryptContentToTicket(content);
        return URLEncoder.encode(encryptStr, TICKET_DEFAULT_CHARSET);
    }

    @Override
    public String decryptTicketToContent(String webTicket) throws Exception{
//        String originalTicket = URLDecoder.decode(webTicket, TICKET_DEFAULT_CHARSET); //web接收的时候已经转化了
        return getTicketParser().decryptTicketToContent(webTicket);
    }
}
