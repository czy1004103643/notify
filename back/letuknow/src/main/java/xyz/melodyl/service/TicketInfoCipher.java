package xyz.melodyl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.melodyl.pojo.ticket.TicketInfo;
import xyz.melodyl.pojo.ticket.TicketParser;

@Service
public class TicketInfoCipher {
    public static final String TICKET_INFO_SEPARATOR = ":";

    @Autowired
    private TicketParser ticketParser;

    // ticket = URLEncoder.encode(AES(userID:tableName))
    public String encryptTicket(TicketInfo ticketInfo) {
        return encryptTicket(ticketInfo.getUserID(), ticketInfo.getTableName());
    }

    public String encryptTicket(Integer userID, String tableName) {
        String content = userID + TICKET_INFO_SEPARATOR + tableName;

        try {
            return ticketParser.encryptContentToTicket(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }

    // index[0]:userID, index[1]:tableName
    public TicketInfo decryptTicket(String ticket) {
        TicketInfo ticketInfo = null;

        try {
            String content = ticketParser.decryptTicketToContent(ticket);
            String[] infoList = content.split(TICKET_INFO_SEPARATOR);
            ticketInfo = new TicketInfo(Integer.valueOf(infoList[0]), infoList[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ticketInfo;
    }
}
