
package com.shine.hotels.controller;

import java.util.HashMap;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class ControllerManager { 
    private static HashMap<String, BaseController> stickyControllers = new HashMap<String, BaseController>();
    
    public static BaseController newController(Context context, Class<?> clazz) {

        try {
            BaseController controller = (BaseController)clazz.newInstance();
            if (controller != null) {
                controller.create(context);

                IntentFilter filter = controller.getFilter();
                if (filter != null) {
                    // context.registerReceiver(controller, filter);
                    LocalBroadcastManager.getInstance(context).registerReceiver(controller, filter);
                }
            }

            return controller;
        } catch (Exception e) {
            // throw new Exception(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
    
    public static BaseController getStickyController(Context context, Class<?> clazz) {
        try {
            String name = clazz.getSimpleName();
            BaseController controller = stickyControllers.get(name);
            if (controller == null) {
                controller = newController(context, clazz);
                
                stickyControllers.put(name, controller);
            }

            return controller;
        } catch (Exception e) {
            // throw new Exception(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
