package xyz.melodyl.pojo.ticket;

import xyz.melodyl.pojo.ticket.TicketParser;

//Decorator pattern
public abstract class DecoratedTicketParser implements TicketParser {
    private TicketParser ticketParser;

    public void setTicketParser(TicketParser ticketParser) {
        this.ticketParser = ticketParser;
    }

    public TicketParser getTicketParser() {
        return ticketParser;
    }
}
