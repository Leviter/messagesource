/**
 * 
 */
package org.synyx.messagesource.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.synyx.messagesource.util.LocaleUtils;


/**
 * Helper-Class that holds informations about a Bundle
 * 
 * @author Marc Kannegiesser - kannegiesser@synyx.de
 */

class BundleInfo {

    Locale locale;
    File file;


    /**
     * Creates a new instance
     * 
     * @param file
     * @param basename
     */
    public BundleInfo(File file, String basename) {

        this.file = file;

        int prefixLength = basename.length();
        int postfixLength = ".properties".length();

        String fileName = file.getName();
        String localeString = fileName.substring(prefixLength, fileName.length() - postfixLength);

        locale = LocaleUtils.toLocale(localeString);
    }


    /**
     * Returns a {@link Map} containing the messages for the given file
     * 
     * @return the messages for the given bundle
     */
    public Map<String, String> getMessages() {

        Properties properties = loadProperties(file);
        Map<String, String> messages = new HashMap<String, String>(properties.size());

        Enumeration<Object> keys = properties.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = (String) properties.getProperty(key);
            messages.put(key, value);
        }

        return messages;
    }


    /**
     * Loads {@link Properties} from the given {@link File} while handling errors.
     * 
     * @param file the file to load from
     * @return the {@link Properties} loaded
     */
    private Properties loadProperties(File file) {

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fis);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Could not load messages from " + file + ": " + e.getMessage(), e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // intentionally left blank
                }
            }
        }

    }

}
