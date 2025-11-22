package com.illtamer.mc.plugin.limit.trade.cmd;

import com.illtamer.mc.plugin.limit.trade.TradeLimit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabHandler
implements TabCompleter {
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        if (!sender.isOp()) {
            return list;
        }
        if (args.length == 1) {
            list.addAll(Arrays.asList("add", "delete", "show"));
        } else if (args.length == 2 && "delete".equals(args[0])) {
            list.add("0-" + TradeLimit.getInstance().getLimitItems().size());
        }
        return list;
    }
}
