/*
 * Copyright 2009-2016 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.component.knob;

import java.awt.Color;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class KnobRenderer extends CoreRenderer {

    public static final String RENDERER_TYPE = "org.primefaces.component.KnobRenderer";

    @Override
    public void decode(FacesContext context, UIComponent component) {

        decodeBehaviors(context, component);

        String submittedValue = (String) context.getExternalContext().getRequestParameterMap().get(component.getClientId(context) + "_hidden");
        ((Knob) component).setSubmittedValue(submittedValue);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        this.encodeMarkup(context, (Knob) component);
        this.encodeScript(context, (Knob) component);
    }

    private void encodeMarkup(FacesContext context, Knob knob) throws IOException {

        ResponseWriter writer = context.getResponseWriter();

        Object value = knob.getValue() != null ? knob.getValue() : 0;

        writer.startElement("input", knob);
        writer.writeAttribute("id", knob.getClientId(), null);
        writer.writeAttribute("name", knob.getClientId(), null);
        writer.writeAttribute("disabled", true, null);
        writer.writeAttribute("value", value.toString(), null);
        writer.writeAttribute("data-min", knob.getMin(), null);
        writer.writeAttribute("data-step", knob.getStep(), null);
        writer.writeAttribute("data-max", knob.getMax(), null);
        writer.writeAttribute("data-displayInput", Boolean.toString(knob.isShowLabel()), null);
        writer.writeAttribute("data-readOnly", Boolean.toString(knob.isDisabled()), null);
        writer.writeAttribute("data-cursor", Boolean.toString(knob.isCursor()), null);
        writer.writeAttribute("data-linecap", knob.getLineCap(), "butt");

        if (knob.getThickness() != null) {
            writer.writeAttribute("data-thickness", knob.getThickness(), null);
        }

        if (knob.getWidth() != null) {
            writer.writeAttribute("data-width", knob.getWidth().toString(), null);
        }

        if (knob.getHeight() != null) {
            writer.writeAttribute("data-height", knob.getHeight().toString(), null);
        }

        writer.writeAttribute("class", "knob", null);

        writer.endElement("input");

        writer.startElement("input", null);
        writer.writeAttribute("id", knob.getClientId() + "_hidden", null);
        writer.writeAttribute("name", knob.getClientId() + "_hidden", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("value", value.toString(), null);
        writer.endElement("input");
    }

    private void encodeScript(FacesContext context, Knob knob) throws IOException {
        String clientId = knob.getClientId();
        String widgetVar = knob.resolveWidgetVar();

        WidgetBuilder wb = getWidgetBuilder(context);

        wb.initWithDomReady("Knob", widgetVar, clientId);
        wb.attr("labelTemplate", knob.getLabelTemplate())
                .attr("colorTheme", knob.getColorTheme())
                .callback("onchange", "function(value)", knob.getOnchange());

        if (knob.getForegroundColor() != null) {
            String fg;
            if (knob.getForegroundColor() instanceof Color) {
                fg = colorToHex((Color) knob.getForegroundColor());
            }
            else {
                fg = knob.getForegroundColor().toString();
            }
            wb.attr("fgColor", fg);
        }

        if (knob.getBackgroundColor() != null) {
            String bg;
            if (knob.getBackgroundColor() instanceof Color) {
                bg = colorToHex((Color) knob.getBackgroundColor());
            }
            else {
                bg = knob.getBackgroundColor().toString();
            }
            wb.attr("bgColor", bg);
        }
        encodeClientBehaviors(context, knob);

        wb.finish();
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        if (submittedValue == null) {
            submittedValue = 0;
        }
        try {
            return ((submittedValue instanceof Integer) ? submittedValue : Integer.parseInt(submittedValue.toString()));
        }
        catch (NumberFormatException e) {
            throw new ConverterException(e);
        }
    }

    public static String colorToHex(Color color) {

        String red = Integer.toHexString(color.getRed());
        if (red.length() < 2) {
            red = "0" + red;
        }

        String blue = Integer.toHexString(color.getBlue());
        if (blue.length() < 2) {
            blue = "0" + blue;
        }

        String green = Integer.toHexString(color.getGreen());
        if (green.length() < 2) {
            green = "0" + green;
        }

        return "#" + red + green + blue;
    }

}
