package xyz.melodyl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.melodyl.pojo.ResponseBuilder;
import xyz.melodyl.pojo.ticket.TicketInfo;
import xyz.melodyl.pojo.table.*;
import xyz.melodyl.service.*;
import xyz.melodyl.annotation.JsonController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static xyz.melodyl.service.UserService.TOKEN_ATTRIBUTE_NAME;

@RestController
@JsonController(value = "/table")
public class TableController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableController.class);

    @Autowired
    @Qualifier("TableService")
    private TableService tableService;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketInfoCipher ticketInfoCipher;

    @PostMapping(value = "/v1")
    public ResponseEntity createTableV1(@RequestHeader(value = TOKEN_ATTRIBUTE_NAME) String token,
                                        @RequestBody SimpleTableContext stc) {
        return doCreateTable(token, stc);
    }

    @PostMapping(value = "/v2")
    public ResponseEntity createTableV2(@RequestHeader(value = TOKEN_ATTRIBUTE_NAME) String token,
                                        @RequestBody ComplicatedTableContext ctc) {
        return doCreateTable(token, ctc);
    }

    private ResponseEntity doCreateTable(String token, BaseTableContext tableContext) {
        ResponseEntity compromiseResponse = forGuest(token);

        if (compromiseResponse != null) { // for guest
            return compromiseResponse;
        }

        Integer userID = getUserIDFromToken(token);
        String tableName = tableContext.getTableName();
        boolean isLegal = BaseTableContext.noIDInColumns(tableContext);

        LOGGER.info("Create Table:userID-{}, tableName-{}");

        if (isLegal && tableService.getTableContext(userID, tableName) == null) {
            tableService.createTableAndAddRecord(userID, tableContext);
            Map body = returnTableAccessUriInfo(userID, tableName);
            return ResponseBuilder.newBuilder(HttpStatus.CREATED)
                    .body(body)
                    .build();
        }

        if (!isLegal) {
            compromiseResponse = ResponseBuilder.badRequest();
        } else {//if != null
            compromiseResponse = ResponseBuilder.found();
        }

        return compromiseResponse;
    }

    @GetMapping(value = "/context")
    public ResponseEntity getTableContextList(@RequestHeader(value = TOKEN_ATTRIBUTE_NAME) String token,
                                              @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                              @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        Integer userID = getUserIDFromToken(token);
        List<ComplicatedTableContext> tcs = tableService.getTableContextList(userID, limit, offset);

        if (tcs != null && tcs.size() > 0) {
            Map<String, String> ticketTableMapper = returnTicketTableMapper(tcs, userID);
            Map data = returnTableContextInfo(ticketTableMapper, tcs);
            return ResponseBuilder.ok(data);
        } else {
            return ResponseBuilder.notFound();
        }
    }

    @GetMapping(value = "/data/{tableName}")
    public ResponseEntity getTableDataList(@RequestHeader(value = TOKEN_ATTRIBUTE_NAME) String token,
                                           @PathVariable("tableName") String tableName,
                                           @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                           @RequestParam(value = "limit", required = false, defaultValue = "50") int limit) {
        Integer userID = getUserIDFromToken(token);
        BaseTableContext tableContext = tableService.getTableContext(userID, tableName);
        List<Map<String, String>> tableContent = null;

        if (tableContext != null) {
            tableContent = tableService.getTableContent(userID, tableContext, limit, offset);
        }

        if (tableContent != null && tableContent.size() > 0)
            return ResponseBuilder.ok(tableContent);
        else
            return ResponseBuilder.notFound();
    }

    /* 以下为白名单，由ticket确认身份，参见#SessionInterceptor#可以了解验证过程 */
    @GetMapping(value = "/context/{ticket}")
    public ResponseEntity getTableContextWithNoCmd(@PathVariable("ticket") String webTicket) {
        TicketInfo ticketInfo = ticketInfoCipher.decryptTicket(webTicket);
        if (ticketInfo != null) {
            BaseTableContext btc = tableService.getTableContextNoCmd(ticketInfo.getUserID(), ticketInfo.getTableName());
            if (btc != null) {
                return ResponseBuilder.ok(btc);
            } else {
                return ResponseBuilder.notFound();
            }
        } else {
            return ResponseBuilder.unAuthorized();
        }
    }

    @PostMapping(value = "/data/{ticket}")
    public ResponseEntity addTableData(@PathVariable("ticket") String webTicket,
                                       @RequestBody TableData tableData) {
        TicketInfo ticketInfo = ticketInfoCipher.decryptTicket(webTicket);
        if (ticketInfo != null) {
            BaseTableContext std = tableService.getTableContext(ticketInfo.getUserID(), ticketInfo.getTableName());
            if (std != null) {
                if (tableData.getCommand().equals(std.getCommand())) {
                    tableService.addTableData(std.buildRealTableName(ticketInfo.getUserID()), tableData);
                    return ResponseBuilder.created();
                } else {
                    return ResponseBuilder.badRequest();
                }
            } else {
                return ResponseBuilder.notFound();
            }
        } else {
            return ResponseBuilder.unAuthorized();
        }
    }

    private Integer getUserIDFromToken(String token) {
        return userService.getUserIDFromToken(token);
    }

    private Map returnTableAccessUriInfo(Integer userID, String tableName) {
        String ticket = ticketInfoCipher.encryptTicket(userID, tableName);
        Map<String, String> temp = new HashMap(1);
        temp.put("ticket", ticket);
        return temp;
    }

    private Map returnTableContextInfo(Map<String, String> ticketTableMapper, List<ComplicatedTableContext> tableContextList) {
        Map<String, Object> temp = new HashMap<>(2);

        temp.put("ticket", ticketTableMapper);
        temp.put("table", tableContextList);

        return temp;
    }

    private Map<String, String> returnTicketTableMapper(List<ComplicatedTableContext> tableContextList, Integer userID) {
        Map<String, String> ticketTableMapper = new HashMap<>(tableContextList.size());

        for (ComplicatedTableContext ctx : tableContextList) {
            String ticket = ticketInfoCipher.encryptTicket(userID, ctx.getTableName());
            ticketTableMapper.put(ctx.getTableName(), ticket);
        }

        return ticketTableMapper;
    }

    private ResponseEntity forGuest(String token) {
        if (UserService.GUEST_TOKEN.equals(token)) {
            return ResponseBuilder.ok(null);
        }

        return null;
    }
}
