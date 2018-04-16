package com.bimapp.model.entity.Template;

import org.json.JSONObject;

public class ImageNode extends TemplateNode {



    public ImageNode(JSONObject node, String title) {
        super(node, title);
    }

    @Override
    public Object getContent() {
        return null;
    }
}
