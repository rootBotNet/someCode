package capitec.services.properties.production;

import capitec.junit.UnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.*;
import java.util.Properties;
import java.util.Set;

@Category(UnitTest.class)
public class PRDServicesPropertiesTest {

    private Properties prop = null;
    private String absolutePath = "";
    private String servicesPropertiesLocation = "container-app-mbank-repo/container-app-mbank-repo-prd/src/main/container/standalone/service/configuration/capitec/services.properties";

    /***
     * Load the production services.properties file.
     * Contained at:
     * container-app-mbank-repo/container-app-mbank-repo-prd/src/main/container/standalone/service/configuration/capitec/
     */
    @Before
    public void setup(){

        this.prop = new Properties();

        absolutePath = new File("").getAbsolutePath();
        absolutePath = absolutePath.replace("\\", "/");
        absolutePath = absolutePath.replaceAll("app-mbank-module", "");

        try {
            prop.load(new FileInputStream( new File(absolutePath + servicesPropertiesLocation)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPRDServicesPropertiesURLCharacters(){

        final String[] urlExcludedRegex = {("-int"), ("-qa"), ("-dr"), ("-hrd"), ("-dev"), ("-prd")};

        Set<Object> keys = prop.keySet();

        /***
         * Iterate through the key values.
         * Evaluate keys ending with uri and url.
         */
        for(Object k:keys){

            String key = (String)k;

            if(key.endsWith("url") || key.endsWith("uri")){

                String value = this.prop.getProperty(key);

                for (String pattern: urlExcludedRegex){
                    if(value.contains(pattern)){

                        System.out.println("***********************************************************");
                        System.out.println("************************** ERROR **************************");
                        System.out.println("***********************************************************");
                        System.out.println("Please ensure that the following configuration is correct.");
                        System.out.println("Production services.properties file location:");
                        System.out.println(absolutePath + servicesPropertiesLocation);
                        System.out.println("services.properties key: " + key + ": " + this.prop.getProperty(key));
                        System.out.println("Production url/i may not contain: " + pattern);

                        Assert.assertFalse(true);
                    }
                }
            }
        }
    }

    @Test
    public void testPRDServicesPropertiesURLBasePath(){

        Set<Object> keys = prop.keySet();

        /***
         * Iterate through the key values.
         * Evaluate keys ending with uri and url.
         */
        for(Object k:keys){

            String key = (String)k;

            if(key.endsWith("url") || key.endsWith("uri")) {

                String value = this.prop.getProperty(key);

                if (value.startsWith("https://ews-echannels.int.capinet/") || value.startsWith("https://ews-dmz202.int.capinet/")
                        || value.startsWith("http://cbwfnlb3.capitecbank.fin.sky:9555/") || value.startsWith("http://FuneralMWServices.int.Capinet:20000/")
                        || value.startsWith("https://creditestimator.int.capinet/")) {

                    Assert.assertTrue(true);

                } else {
                    System.out.println("***********************************************");
                    System.out.println("******************** ERROR ********************");
                    System.out.println("***********************************************");
                    System.out.println("Please ensure that the following configuration is correct.");
                    System.out.println("Production services.properties file location:");
                    System.out.println(absolutePath + servicesPropertiesLocation);
                    System.out.println("services.properties key: " + key + ": " + this.prop.getProperty(key));
                    Assert.assertTrue(false);

                }
            }
        }
    }
}
