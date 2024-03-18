package dk.anderz.blomsterplukning.utils;

import dk.anderz.blomsterplukning.BlomsterPlukning;
import org.bukkit.OfflinePlayer;

public class Econ {

    public static boolean addMoney(OfflinePlayer player, Double amount) {
        return BlomsterPlukning.econ.depositPlayer(player, amount).transactionSuccess();
    }

    public static boolean removeMoney(String player, double amount) {
        return BlomsterPlukning.econ.withdrawPlayer(player, amount).transactionSuccess();
    }

    private boolean addMoneyToPlayer(String playerName, double amount) {
        return BlomsterPlukning.econ.depositPlayer(playerName, amount).transactionSuccess();
    }

    public static double getbalance(String playerName) {
        return BlomsterPlukning.econ.getBalance(playerName);
    }
}
