package xyz.melodyl.pojo.ticket;

public interface TicketParser {
    String TICKET_DEFAULT_CHARSET = "UTF-8";

    String encryptContentToTicket(String content) throws Exception;
    String decryptTicketToContent(String ticket) throws Exception;
}
