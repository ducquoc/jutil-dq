package vn.ducquoc.activiti.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Configuration Manager for multi-tenant configs.<br/>
 * The meta-config file will point to the path of each tenant's config file. By
 * default it uses the config file in classpath (JAR), but will allow external
 * file to override the configuration.
 */
public class ConfigManager implements Serializable {

    private static final long serialVersionUID = 6433576686063538252L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class.getName());

    private static final String DEFAULT_CONFIG_FILE = "config_manager.yaml";

    // default: use the one in classpath (JAR)
    private static final String DEFAULT_META_CONFIG = Thread.currentThread().getContextClassLoader()
            .getResource(DEFAULT_CONFIG_FILE).getPath();

    // runtime: override with external file
    private static final String META_CONFIG = "../conf/" + DEFAULT_CONFIG_FILE;

    private static ConfigManager instance;

    // Map of contents of file named by META_CONFIG
    private static Map<String, String> tenantCfgPath = new HashMap<String, String>();

    // Map of ClientNames to their corresponding WorkflowConfiguration object
    private static Map<String, TenantConfig> tenantConfig = new HashMap<String, TenantConfig>();

    public static synchronized ConfigManager getInstance() {
        if (instance == null) { // simple singleton
            instance = new ConfigManager();
        }

        return instance;
    }

    protected ConfigManager() {
        LOGGER.info("Reading meta-config from file...");
        File metaConfig = new File(META_CONFIG);
        File defaultMetaConfig = new File(DEFAULT_META_CONFIG);

        if (metaConfig.exists() && metaConfig.isFile()) {
            LOGGER.info("Populating config from file path {}", metaConfig.getAbsolutePath());
            loadMetaConfig(metaConfig);

        } else if (defaultMetaConfig.exists() && defaultMetaConfig.isFile()) {
            LOGGER.info("Populating default config from file path {}", defaultMetaConfig.getAbsolutePath());
            loadMetaConfig(defaultMetaConfig);

        } else {
            LOGGER.warn("Unable to read meta-config file {}", META_CONFIG);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadMetaConfig(File metaCfgFile) {
        Yaml metaYaml = new Yaml();

        try {
            InputStream fIStream = new FileInputStream(metaCfgFile);
            InputStreamReader ins = new InputStreamReader(fIStream, "UTF-8");
            tenantCfgPath = (HashMap<String, String>) metaYaml.load(ins);

        } catch (IOException ex) { // log-and-throw for easy debugging
            LOGGER.error("Caught IOException: {}", ex.getMessage(), ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }

        LOGGER.debug(metaYaml.dump(tenantCfgPath));

        Pattern pathRegex = Pattern.compile("^resource\\:///(\\S+)");
        String resourceBase = this.getClass().getClassLoader().getResource("").getPath();
        for (String tenantName : tenantCfgPath.keySet()) {
            String configPath = tenantCfgPath.get(tenantName);
            Matcher pathMatcher = pathRegex.matcher(configPath);
            if (pathMatcher.find()) { // convert resource path
                String origConfigPath = configPath;
                configPath = resourceBase + pathMatcher.group(1);
                tenantCfgPath.put(tenantName, configPath);
                LOGGER.debug("Converted original value for {} from : {} to : {}", new Object[] { tenantName,
                        origConfigPath, configPath });
            }

            LOGGER.debug("Tenant : {} Cfg File : {}", tenantName.toString(), configPath);
            loadConfig(tenantName, configPath);
        }

    }

    public void loadConfig(String tenantName, String configPath) {
        File configFile = new File(configPath);

        if (configFile.exists() && configFile.isFile()) {
            LOGGER.info(" - Loading config for {} from file path {}", tenantName, configFile.getAbsolutePath());

            Constructor wfCon = new Constructor(TenantConfig.class);
            TypeDescription wfDesc = new TypeDescription(TenantConfig.class);
            wfCon.addTypeDescription(wfDesc);

            Yaml yaml = new Yaml(wfCon);

            try {
                InputStream cfStream = new FileInputStream(configFile);
                TenantConfig thisWfCfg = (TenantConfig) yaml.load(cfStream);
                tenantConfig.put(tenantName, thisWfCfg);
            } catch (IOException e) {
                LOGGER.error("Caught IOException: {}", e.getMessage(), e);
                throw new RuntimeException(e.getMessage() ,e);
            }

        } else {
            LOGGER.warn("Unable to read config file {} for client {}", configPath, tenantName);
        }

    }

    public void loadConfig(String tenantName) {
        if (tenantExists(tenantName)) {
            String configPath = tenantCfgPath.get(tenantName);
            loadConfig(tenantName, configPath);
            LOGGER.info("Reloaded config for Client : {}", tenantName);
        }
    }

    public boolean tenantExists(String clientName) {
        return tenantCfgPath.containsKey(clientName);
    }

    // extra interfaces
    public void loadConfig(String tenantName, InputStream yamlStream) throws Exception {
        LOGGER.info(" - Loading config for {} from prepared input stream", tenantName);

        Constructor wfCon = new Constructor(TenantConfig.class);
        TypeDescription wfDesc = new TypeDescription(TenantConfig.class);
        wfCon.addTypeDescription(wfDesc);

        Yaml yaml = new Yaml(wfCon);

        TenantConfig thisWfCfg = (TenantConfig) yaml.load(yamlStream);
        tenantConfig.put(tenantName, thisWfCfg);
    }

    public TenantConfig getTenantConfig(String tenantName) {
        LOGGER.debug("getTenantConfig - {}", tenantName);
        return tenantConfig.get(tenantName);
    }

}
