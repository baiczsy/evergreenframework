package org.evergreen.web.view;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.evergreen.web.ActionContext;
import org.evergreen.web.ViewResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;

/**
 * Volecity 模板
 * Created by wangl on 2016/7/5.
 */
public class VelocityTemplate extends ViewResult {

    static {
        try {
            initVelocity();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String vmpath;

    private VelocityContext context;

    public VelocityTemplate(String vmpath, VelocityContext context){
        this.vmpath = vmpath;
        this.context = context;
    }

    public void setVmpath(String vmpath) {
        this.vmpath = vmpath;
    }

    public void setContext(VelocityContext context) {
        this.context = context;
    }

    @Override
    protected void execute() throws IOException {
        Template template = Velocity.getTemplate(vmpath);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        getResponse().setContentType("text/html;charset=utf-8");
        getResponse().getWriter().println(writer.toString());
    }

    /**
     * 初始化Velocity
     * @throws IOException
     */
    private static void initVelocity() throws IOException {
        InputStream in = VelocityTemplate.class.getClassLoader().getResourceAsStream("velocity.properties");
        Properties prop = new Properties();
        prop.load(in);
        for (String name : prop.stringPropertyNames()) {
            if(name.equals(Velocity.FILE_RESOURCE_LOADER_PATH)){
                String projectDir = ActionContext.getContext().getRealPath("");
                Velocity.addProperty(name, projectDir + prop.getProperty(name));
            } else {
                Velocity.addProperty(name, prop.getProperty(name));
            }
        }
        Velocity.init();
    }
}