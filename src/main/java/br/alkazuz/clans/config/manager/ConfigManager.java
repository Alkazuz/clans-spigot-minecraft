package br.alkazuz.clans.config.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import br.alkazuz.clans.Clans;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class ConfigManager {

	public static void createConfig(String file) {
		if (!new File(Clans.getInstance().getDataFolder(), file + ".yml").exists()) {
			Clans.getInstance().saveResource(file + ".yml", false);
		}
	}
	
	public static FileConfiguration getConfig(String file) {
      	try {
      		File arquivo = new File(Clans.getInstance().getDataFolder() + File.separator + file + ".yml");
			InputStreamReader arquivoStream = new InputStreamReader(new FileInputStream(arquivo), Charset.forName("UTF-8").name());
			FileConfiguration config = (FileConfiguration)YamlConfiguration.loadConfiguration(arquivo);
			return config;
		} catch (Throwable e) {
			e.printStackTrace();
		} 
      	return null;
	}
	
}