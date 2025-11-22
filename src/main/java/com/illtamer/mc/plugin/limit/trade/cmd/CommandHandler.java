package com.illtamer.mc.plugin.limit.trade.cmd;

import com.illtamer.mc.plugin.limit.trade.TradeLimit;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandHandler
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only player can use this command");
            return true;
        }
        if (!sender.isOp()) {
            sender.sendMessage("No permission");
            return true;
        }
        Player player = (Player)sender;
        if (args.length == 1 && "add".equals(args[0])) {
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            if (mainHand.getType().isAir() || mainHand.getAmount() == 0) {
                player.sendMessage("Please take a item in main hand");
                return true;
            }
            mainHand.setAmount(1);
            TradeLimit.getInstance().getLimitItems().add(mainHand);
            TradeLimit.getInstance().saveConfig();
            player.sendMessage("Add item success");
        } else if (args.length == 2 && "delete".equals(args[0])) {
            Integer index = CommandHandler.parseInt(args[1]);
            if (index == null) {
                player.sendMessage("Wrong args: " + args[1]);
                return true;
            }
            List<ItemStack> limitItems = TradeLimit.getInstance().getLimitItems();
            if (index < 0 || index >= limitItems.size()) {
                player.sendMessage("Out of index");
                return true;
            }
            limitItems.remove(index);
            player.sendMessage("Delete success");
        } else if (args.length == 1 && "show".equals(args[0])) {
            StringBuilder builder = new StringBuilder();
            List<ItemStack> limitItems = TradeLimit.getInstance().getLimitItems();
            for (int i = 0; i < limitItems.size(); ++i) {
                builder.append(i).append(": ").append(limitItems.get(i).toString()).append('\n');
            }
            player.sendMessage(builder.toString());
        }
        return true;
    }

    private static Integer parseInt(String s) {
        try {
            return Integer.parseInt(s);
        }
        catch (Exception ignore) {
            return null;
        }
    }
}
