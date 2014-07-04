/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.cascadeeffects;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.calendar.SingleDaySelectionModel;

import java.awt.Color;
import java.awt.FlowLayout;

import java.text.DateFormat;
import java.text.ParseException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

/**
 * This is licensed under LGPL. License can be found here: http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * <p>This is provided as is. If you have questions please direct them to charlie.hubbard at gmail dot you know what.
 * </p>
 *
 * <p>Edited by mscholl</p>
 *
 * @version  $Revision$, $Date$
 */
// TODO: time can currently not be changed by direct entry + enter
public class DateTimePicker extends JXDatePicker {

    //~ Instance fields --------------------------------------------------------

    private JSpinner timeSpinner;
    private JPanel timePanel;
    private DateFormat timeFormat;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DateTimePicker object.
     */
    public DateTimePicker() {
        super();
        getMonthView().setSelectionModel(new SingleDaySelectionModel());
    }

    /**
     * Creates a new DateTimePicker object.
     *
     * @param  d  DOCUMENT ME!
     */
    public DateTimePicker(final Date d) {
        this();
        setDate(d);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void commitEdit() throws ParseException {
        commitTime();
        super.commitEdit();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setTimeSpinners();
    }

    @Override
    public JPanel getLinkPanel() {
        super.getLinkPanel();
        if (timePanel == null) {
            timePanel = createTimePanel();
        }
        setTimeSpinners();
        return timePanel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private JPanel createTimePanel() {
        final JPanel newPanel = new JPanel();
        newPanel.setLayout(new FlowLayout());
        // newPanel.add(panelOriginal);

        final SpinnerDateModel dateModel = new SpinnerDateModel();
        timeSpinner = new JSpinner(dateModel);
        if (timeFormat == null) {
            timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        }
        updateTextFieldFormat();
        newPanel.add(new JLabel("Time:"));
        newPanel.add(timeSpinner);
        newPanel.setBackground(Color.WHITE);
        return newPanel;
    }

    /**
     * DOCUMENT ME!
     */
    private void updateTextFieldFormat() {
        if (timeSpinner == null) {
            return;
        }
        final JFormattedTextField tf = ((JSpinner.DefaultEditor)timeSpinner.getEditor()).getTextField();
        final DefaultFormatterFactory factory = (DefaultFormatterFactory)tf.getFormatterFactory();
        final DateFormatter formatter = (DateFormatter)factory.getDefaultFormatter();
        // Change the date format to only show the hours
        formatter.setFormat(timeFormat);
    }

    /**
     * DOCUMENT ME!
     */
    private void commitTime() {
        final Date date = getDate();
        System.out.println(timePanel.isVisible());
        if (date != null) {
            final Date time = (Date)timeSpinner.getValue();
            final GregorianCalendar timeCalendar = new GregorianCalendar();
            timeCalendar.setTime(time);

            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            final Date newDate = calendar.getTime();
            setDate(newDate);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void setTimeSpinners() {
        final Date date = getDate();
        if (date != null) {
            timeSpinner.setValue(date);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public DateFormat getTimeFormat() {
        return timeFormat;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  timeFormat  DOCUMENT ME!
     */
    public void setTimeFormat(final DateFormat timeFormat) {
        this.timeFormat = timeFormat;
        updateTextFieldFormat();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        final Date date = new Date();
        final JFrame frame = new JFrame();
        frame.setTitle("Date Time Picker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final DateTimePicker dateTimePicker = new DateTimePicker();
        dateTimePicker.setFormats(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT));
        dateTimePicker.setTimeFormat(DateFormat.getTimeInstance(DateFormat.SHORT));

        dateTimePicker.setDate(date);

        frame.getContentPane().add(dateTimePicker);
        frame.pack();
        frame.setVisible(true);
    }
}
