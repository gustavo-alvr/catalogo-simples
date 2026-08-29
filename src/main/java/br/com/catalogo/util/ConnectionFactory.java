package br.com.catalogo.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Fábrica de conexões JDBC para o banco MySQL.
 * <p>
 * Lê configurações de {@code db.properties} no classpath.
 * Permite sobrescrita via variáveis de ambiente: DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD.
 * </p>
 */
public final class ConnectionFactory {

    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final Properties PROPS = new Properties();

    private static String host;
    private static String port;
    private static String dbName;
    private static String user;
    private static String password;
    private static String useSSL;
    private static String serverTimezone;
    private static String characterEncoding;

    static {
        try (InputStream is = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                PROPS.load(is);
            } else {
                LOGGER.warning("db.properties não encontrado no classpath. Tentando variáveis de ambiente.");
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Erro ao carregar db.properties", e);
        }

        host = getConfig("db.host", "DB_HOST", "localhost");
        port = getConfig("db.port", "DB_PORT", "3306");
        dbName = getConfig("db.name", "DB_NAME", "catalogo_db");
        user = getConfig("db.user", "DB_USER", "root");
        password = getConfig("db.password", "DB_PASSWORD", "root");
        useSSL = getConfig("db.useSSL", "DB_USE_SSL", "false");
        serverTimezone = getConfig("db.serverTimezone", "DB_TIMEZONE", "America/Sao_Paulo");
        characterEncoding = getConfig("db.characterEncoding", "DB_ENCODING", "UTF-8");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Driver MySQL não encontrado", e);
        }
    }

    private ConnectionFactory() {
        // utilitária
    }

    private static String getConfig(String propKey, String envKey, String defaultValue) {
        String env = System.getenv(envKey);
        if (env != null && !env.isBlank()) {
            return env;
        }
        return PROPS.getProperty(propKey, defaultValue);
    }

    /**
     * Cria e retorna uma nova conexão JDBC.
     *
     * @return conexão aberta
     * @throws SQLException se não for possível conectar
     */
    public static Connection getConnection() throws SQLException {
        String url = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=%s&serverTimezone=%s&characterEncoding=%s&allowPublicKeyRetrieval=true",
                host, port, dbName, useSSL, serverTimezone, characterEncoding
        );
        return DriverManager.getConnection(url, user, password);
    }
}
