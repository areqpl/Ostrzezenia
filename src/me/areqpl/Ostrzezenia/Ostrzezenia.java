package me.areqpl.Ostrzezenia;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Ostrzezenia extends JavaPlugin {
	public static Ostrzezenia plugin;
	Logger log;
    public static Economy econ = null;
	public String bold = ""+ChatColor.BOLD;
	public String boldRed = bold+""+ChatColor.RED;
	public String white = ""+ChatColor.WHITE;
	int Ostrzezenia;
	String powod,ban;
	int sec,min,h;
	FileConfiguration gec = getConfig();
    /*************************************************************/
    /* SETUP ECO,CHAT,PERM */
    /*************************************************************/
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    /*************************************************************/
    /* SETUP ECO,CHAT,PERM */
    /*************************************************************/
    public void LoadConfiguration(){
    	gec.addDefault("zgloszenie", 100);
    	gec.addDefault("czasbana", 1800);
    	this.gec.options().copyDefaults(true);
    	this.saveConfig();
    }
	public void onEnable(){
		LoadConfiguration();
		log = this.getLogger();
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info(pdfFile.getName() + " version: v" + pdfFile.getVersion()+" enabled.");
        setupEconomy();
	}
	public void onDisable(){
		log = this.getLogger();
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info(pdfFile.getName() + " version: v" + pdfFile.getVersion()+" disabled.");
		saveConfig();
}
	public void redMessage(String string, CommandSender sender){
	     sender.sendMessage(ChatColor.RED+"");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String komenda, String[] args){
		//Player player = (Player) sender;  //nick wysylajacego w postaci player
	    //String nick = player.getDisplayName(); //nick wysylajacego w postaci string
	    if (komenda.equalsIgnoreCase("ostrzezenie")){
	    	if (args.length==0) sender.sendMessage("Dostepne komendy:\n"+boldRed+"/ostrzezenie dodaj <gracz> <powod>"+white+" - dodaj komus ostrzezenie\n"+boldRed+"/ostrzezenie odejmij <gracz> <powod>"+white+" - zabierz komus ostrzezenie\n"+boldRed+"/ostrzezenie wyczysc <gracz> <powod>"+white+" - wyczysc status ostrzezen danego gracza\n"+boldRed+"/ostrzezenie status"+white+" - wyswietl status ostrzezen danej osoby");
	    	if (args.length==1){
	    		if (args[0].equalsIgnoreCase("dodaj")||args[0].equalsIgnoreCase("odejmij")||args[0].equalsIgnoreCase("wyczysc")) redMessage("Wprowadz nick, wartosc ostrzezenia i powod", sender);
	    		if (args[0].equalsIgnoreCase("status")) redMessage("Wprowadz nick osoby ktora chcesz sprawdzic", sender);
	    		else redMessage("Nie ma takiej komendy!", sender);
	    	}
	    	if (args.length==2){
	    		if (args[0].equalsIgnoreCase("dodaj")||args[0].equalsIgnoreCase("odejmij")) redMessage("Wprowadz nick, wartosc ostrzezenia i powod", sender);
	    		if (args[0].equalsIgnoreCase("status")) redMessage("Wprowadz nick osoby ktora chcesz sprawdzic", sender);
	    		else redMessage("Nie ma takiej komendy!", sender);
	    	}
	    	if (args.length>=2){
				Player gracz=getServer().getPlayer(args[1]);
				String graczOstrzezenia="gracz."+args[1]+".ostrzezenia";
				String graczPowody="gracz."+args[1]+".powody";
    			if ((!gracz.isOp())&&(!gracz.isPermissionSet("ostrzezenia.odporny"))){
	    		//try{ Wartosc=Integer.parseInt(args[1]); } catch(NumberFormatException e){redMessage("Wprowadz prawidlowa liczbe.", sender);return false;};
    				powod=sender.getName()+" ";
    				for (int i=2;args.length>i;i++) powod=powod+args[i];
    				if (args[0].equalsIgnoreCase("dodaj")){
    					if (gec.getInt(graczOstrzezenia)>=2){
    						gec.set(graczOstrzezenia, 3);
    						gec.set(graczPowody, powod);
    						sender.sendMessage(ChatColor.GREEN+"Gracz ma 3 ostrzezenia. Czy chcesz go automatycznie ukarac i zresetowac ostrzezenia do 0?\nNapisz /tak lub /nie");
    						if (komenda.equalsIgnoreCase("tak")){
    							sec=gec.getInt("czasbana");
    							ban="tempban "+gracz+" "+sec;
    							getServer().dispatchCommand(this.getServer().getConsoleSender(), ban);
    							gec.set(graczOstrzezenia, 0);
    							return true;
    						}
    						if (komenda.equalsIgnoreCase("nie")){
    							sender.sendMessage("Gracz "+gracz.getDisplayName()+ " nie zostal ukarany.\nMa on juz maksymalna liczbe ostrzezen.");
    							return true;
    						}
    						else {
    							redMessage("Blad: nie wprowadziles poprawnej odpowiedzi", sender);
    						}
    					}
    					
    				
    				}
    				if (args[0].equalsIgnoreCase("odejmij")){	
    					
    				}
	    		}
    			else redMessage("Blad: Gracz jest adminem!", sender);
	    	}
	    }
	    return false;
	}
}
/*if (sec>60){   funkcja na czas, moze sie kiedys przydac!
min=sec/60;
sec=sec%60;
if (sec>3600){
	h=min/60;    //godziny to minuty/60
}
}*/