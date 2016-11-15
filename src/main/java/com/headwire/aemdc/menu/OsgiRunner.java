package com.headwire.aemdc.menu;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.headwire.aemdc.command.CommandMenu;
import com.headwire.aemdc.command.CreateFileCommand;
import com.headwire.aemdc.command.ReplacePlaceHoldersCommand;
import com.headwire.aemdc.companion.Constants;
import com.headwire.aemdc.companion.Resource;
import com.headwire.aemdc.util.ConfigUtil;


/**
 * OSGI creator
 *
 */
public class OsgiRunner extends BasisRunner {

  // Invoker
  private final CommandMenu menu = new CommandMenu();

  /**
   * Constructor
   *
   * @param resource
   *          - resource object
   * @throws IOException
   *           - IOException
   */
  public OsgiRunner(final Resource resource) throws IOException {
    // Get Config Properties from config file
    final Properties configProps = ConfigUtil.getConfigProperties();

    // set target folder patch
    resource.setSourceFolderPath(configProps.getProperty(Constants.CONFIGPROP_SOURCE_OSGI_FOLDER));
    final Map<String, String> commonJcrProps = resource.getJcrPropsSet(Constants.PLACEHOLDERS_PROPS_SET_COMMON);
    final String runmode = commonJcrProps.get(Constants.PARAM_RUNMODE);
    if (StringUtils.isNotBlank(runmode)) {
      // add config.<runmode> to the target path
      resource
          .setTargetFolderPath(configProps.getProperty(Constants.CONFIGPROP_TARGET_OSGI_FOLDER) + "." + runmode);
    } else {
      resource.setTargetFolderPath(configProps.getProperty(Constants.CONFIGPROP_TARGET_OSGI_FOLDER));
    }

    checkConfiguration(configProps, resource);

    // Set global config properties in the resource
    setGlobalConfigProperties(configProps, resource);

    // Creates Invoker object, command object and configure them
    menu.setCommand("CreateFile", new CreateFileCommand(resource));
    menu.setCommand("ReplacePlaceHolders", new ReplacePlaceHoldersCommand(resource));
  }

  /**
   * Run commands
   *
   * @throws IOException
   */
  @Override
  public void run() throws IOException {
    // Invoker invokes command
    menu.runCommand("CreateFile");
    menu.runCommand("ReplacePlaceHolders");
  }

}
