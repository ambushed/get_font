package com.ui;

import com.PlatformTextField;
import com.ProductVolumeUIModel;
import com.ProductsVolumeManager;
import com.BarakBeanProperty;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jfree.ui.NumberCellRenderer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: Yuval
 * Date: 08/08/12
 */
public class ProductsVolumeUI {
    private static final Logger log = Logger.getLogger(ProductsVolumeUI.class);

    class ProductVolumeUITableSelectionHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            int total = 0;
            int col = -1;
            String text = dcLabel.getText();
            text = text.substring(0, text.indexOf(':') + 1);

            for (int i = 0; i < table.getColumnCount(); i++)
                if (table.getColumnName(i).equals("DC Net")) {
                    col = i;
                    break;
                }

            int rows[] = table.getSelectedRows();

            if (col != -1 && rows.length != 0) {
                for (int row : rows)
                    total += (int) table.getValueAt(row, col);

                text += " " + total;
            }

            text += "    ";
            dcLabel.setText(text);

            if (adminPanel.isVisible()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    final String exchange = table.getValueAt(row, 0).toString();
                    final String productCode = table.getValueAt(row, 1).toString();

                    exchangeCodeDealLabel.setText(exchange + " - " + productCode);
                } else {
                    exchangeCodeDealLabel.setText("");
                }
            }
        }
    }

    private JPanel mainPanel;
    private JXTable table;

    private JPanel exchangesPanel;
    private JFrame filtersWindowFrame;

    // JIRA IN-100 YAZR filter the display of the exchanges

    // private JPanel exchangesFilterPanel;
    private JLabel dcLabel;
    private JButton sendPositionsMailBtn;
    private JCheckBox diffCheckBox;
    private JPanel adminPanel;
    private JButton addManualDealBtn;
    private JCheckBox enableAddDealCB;
    private JComboBox<Integer> dealActionCombo;
    private JComboBox<Integer> dealTradableTypeCombo;
    private JComboBox<Integer> dealSourceCombo;
    private JLabel exchangeCodeDealLabel;
    private JButton btnReport;
    private JButton FiltersBtn;
    private PlatformTextField<Integer> dealAmountTF;
    private PlatformTextField<Float> dealPriceTF;
    private PlatformTextField<String> dealTradableIdTF;
    private PlatformTextField<String> dealUserOrPortfolioTF;
    final Set<String> exchangesFiltered = new HashSet<>();

    private final TabType tabType;
    private final boolean isStocksTab;
    private static final boolean isBTSForInterest = true;

    /**
     * The type of tab
     */
    public static enum TabType {
        DERIVATIVES, STOCKS, COUNTER
    }

    /**
     * The location of the previous window created
     */
    final Point pointOfPreviousWindow;
    private final ProductsVolumeManager manager;
    public ProductsVolumeUI(final ProductsVolumeManager manager, final TabType tabType, final Point pointOfMainWindow) {
        this.manager = manager;
        this.tabType = tabType;
        this.isStocksTab = tabType == TabType.STOCKS;
        this.pointOfPreviousWindow = pointOfMainWindow;

        bindExchangesPanel();
        bindTable();

        setListeners();
    }

    private void bindExchangesPanel() {
    }

    private boolean shouldHighlightRecentlyChangedRow(final int index) {
        return false;
    }

    private boolean shouldHighlightPSCell(final int index, int shift) {
        return true;
    }

    private boolean shouldHighlightTotalCell(final int index) {
        return true;
    }

    private boolean shouldHighlightDCCell(int index) {
        return true;
    }

    private void bindTable() {
        final JTableBinding<ProductVolumeUIModel, List<ProductVolumeUIModel>, JTable> tb = SwingBindings.createJTableBinding(AutoBinding.UpdateStrategy.READ_WRITE, manager.getProductVolumesUIList(isStocksTab), table);

        tb.addColumnBinding(BarakBeanProperty.property(ProductVolumeUIModel.class, "exchange", String.class)).setColumnName("Exchange").setColumnClass(String.class).setEditable(false);
        tb.addColumnBinding(BarakBeanProperty.property(ProductVolumeUIModel.class, "productCode", String.class)).setColumnName("Code").setColumnClass(String.class).setEditable(false);
        tb.addColumnBinding(BarakBeanProperty.property(ProductVolumeUIModel.class, "baseAssetNameMapping", String.class)).setColumnName("Asset Name").setColumnClass(String.class).setEditable(false);
        tb.addColumnBinding(BarakBeanProperty.property(ProductVolumeUIModel.class, "buyAmount", Integer.class)).setColumnName("DC Buy").setColumnClass(Integer.class).setEditable(false);
        tb.addColumnBinding(BarakBeanProperty.property(ProductVolumeUIModel.class, "sellAmount", Integer.class)).setColumnName("DC Sell").setColumnClass(Integer.class).setEditable(false);
        tb.addColumnBinding(BarakBeanProperty.property(ProductVolumeUIModel.class, "totalAmount", Integer.class)).setColumnName("DC Total").setColumnClass(Integer.class).setEditable(false);
        tb.addColumnBinding(BarakBeanProperty.property(ProductVolumeUIModel.class, "netAmount", Integer.class)).setColumnName("DC Net").setColumnClass(Integer.class).setEditable(false);
        tb.addColumnBinding(BarakBeanProperty.property(ProductVolumeUIModel.class, "positionServerNetAmount", Integer.class)).setColumnName("PS Net").setColumnClass(Integer.class).setEditable(false);
        if (isBTSForInterest) {
            tb.addColumnBinding(BarakBeanProperty.property(ProductVolumeUIModel.class, "positionServerTotalAmount", Integer.class)).setColumnName("PS Total").setColumnClass(Integer.class).setEditable(false);
            tb.addColumnBinding(BarakBeanProperty.property(ProductVolumeUIModel.class, "positionServerSellAmount", Integer.class)).setColumnName("PS Sell").setColumnClass(Integer.class).setEditable(false);
            tb.addColumnBinding(BarakBeanProperty.property(ProductVolumeUIModel.class, "positionServerBuyAmount", Integer.class)).setColumnName("PS Buy").setColumnClass(Integer.class).setEditable(false);
        }
        tb.addColumnBinding(BarakBeanProperty.property(ProductVolumeUIModel.class, "lastChange", String.class)).setColumnName("Change").setColumnClass(String.class).setEditable(false);


        table.getTableHeader().setReorderingAllowed(false);

        // Format the int columns in the table and keep columns highlighted even when they are coloured
        table.setDefaultRenderer(Integer.class, new NumberCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cellRendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                //final String fontName = getFont().getName();
                setFont(new Font(Font.DIALOG, Font.BOLD, 14));

                if (shouldHighlightRecentlyChangedRow(table.convertRowIndexToModel(row)))
                    setBackground(new Color(179, 255, 236));
                if (column == 6 && shouldHighlightDCCell(table.convertRowIndexToModel(row)))
                    setBackground(new Color(255, 153, 153));
                if (column == 7 && shouldHighlightPSCell(table.convertRowIndexToModel(row), 0))
                    setBackground(new Color(255, 153, 153));
                if (isBTSForInterest) {
                    if (column == 8 && shouldHighlightPSCell(table.convertRowIndexToModel(row), 1))
                        setBackground(new Color(255, 153, 153));
                    if (column == 9 && shouldHighlightPSCell(table.convertRowIndexToModel(row), 2))
                        setBackground(new Color(255, 153, 153));
                    if (column == 10 && shouldHighlightPSCell(table.convertRowIndexToModel(row), 3))
                        setBackground(new Color(255, 153, 153));
                }
                if (column == 5 && shouldHighlightTotalCell(table.convertRowIndexToModel(row)))
                    setBackground(Color.yellow);

                return cellRendererComponent;
            }
        });

        // Highlight a recently changed row
        table.addHighlighter(new ColorHighlighter((component, componentAdapter) -> {
            final int index = table.convertRowIndexToModel(componentAdapter.row);
            return index >= 0 && componentAdapter.column != 6 && componentAdapter.column != 7 && shouldHighlightRecentlyChangedRow(index);
        }, new Color(179, 255, 236), null));

        // Highlight a column that violates max in day activity guard
        table.addHighlighter(new ColorHighlighter((component, componentAdapter) -> {
            if (componentAdapter.column == 6) {
                final int index = table.convertRowIndexToModel(componentAdapter.row);
                if (index >= 0) {
                    return shouldHighlightDCCell(index);
                }
            }
            return false;
        }, new Color(255, 153, 153), null));

        // Highlight a column where the net values from the Drop Copy and Position Server are different
        table.addHighlighter(new ColorHighlighter((component, componentAdapter) -> {
            if (componentAdapter.column == 7) {
                final int index = table.convertRowIndexToModel(componentAdapter.row);
                if (index >= 0) {
                    return shouldHighlightPSCell(index, 0);
                }
            }
            return false;
        }, new Color(255, 153, 153), null));

        if (isBTSForInterest) {
            // Highlight a column where the total values from the Drop Copy and Position Server are different
            table.addHighlighter(new ColorHighlighter((component, componentAdapter) -> {
                if (componentAdapter.column == 8) {
                    final int index = table.convertRowIndexToModel(componentAdapter.row);
                    if (index >= 0) {
                        return shouldHighlightPSCell(index, 1);
                    }
                }
                return false;
            }, new Color(255, 153, 153), null));

            // Highlight a column where the Sell values from the Drop Copy and Position Server are different
            table.addHighlighter(new ColorHighlighter((component, componentAdapter) -> {
                if (componentAdapter.column == 9) {
                    final int index = table.convertRowIndexToModel(componentAdapter.row);
                    if (index >= 0) {
                        return shouldHighlightPSCell(index, 2);
                    }
                }
                return false;
            }, new Color(255, 153, 153), null));

            // Highlight a column where the Buy values from the Drop Copy and Position Server are different
            table.addHighlighter(new ColorHighlighter((component, componentAdapter) -> {
                if (componentAdapter.column == 10) {
                    final int index = table.convertRowIndexToModel(componentAdapter.row);
                    if (index >= 0) {
                        return shouldHighlightPSCell(index, 3);
                    }
                }
                return false;
            }, new Color(255, 153, 153), null));
        }
        // Highlight a column where the daily number of trades is more than the moving average
        table.addHighlighter(new ColorHighlighter((component, componentAdapter) -> {
            if (componentAdapter.column == 5) {
                final int index = table.convertRowIndexToModel(componentAdapter.row);
                return shouldHighlightTotalCell(index);
            }
            return false;
        }, Color.yellow, null));

        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(new ProductVolumeUITableSelectionHandler());

        table.addKeyListener(new KeyListener() {
            @Override public void keyTyped(final KeyEvent e) { /* Do nothing */ }
            @Override public void keyPressed(final KeyEvent e) { /* Do nothing */ }
            @Override public void keyReleased(final KeyEvent e) {
                final int row = table.getSelectedRow();
                if (row > -1 && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    openDetailsWindows(row);
                } else if (row > -1 && e.getKeyCode() == KeyEvent.VK_SPACE) {
                    openExpiryCompareWindow(row);
                }
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(final MouseEvent e) {
                if (e.getClickCount() == 2) {
                    final int row = table.rowAtPoint(e.getPoint());
                    if (row > -1) {
                        if (e.getButton() == 1)
                            openDetailsWindows(row);
                        else openExpiryCompareWindow(table.rowAtPoint(e.getPoint()));
                    }
                }
            }
        });

        sendPositionsMailBtn.addActionListener(e -> {
        });

        btnReport.addActionListener(e -> {
        });

        FiltersBtn.addActionListener(e -> {
            openFiltersWindow();
        });
    }

    private void openFiltersWindow() {
    }


    private void openDetailsWindows(final int row) {
        try {
        } catch (final NullPointerException e) {
            //Do nothing, this is OK since there are rows without an asset name (asset name = null)
        }
    }

    private void openExpiryCompareWindow(int row) {
        final String exchange = table.getValueAt(row, 0).toString();
        final String baseAssetName = table.getValueAt(row, 2).toString();
        List<ExpiresDifferent> expiresDifference = fillList(exchange, baseAssetName);
        displayExpiresDiffDetails(expiresDifference, exchange, baseAssetName);
    }

    public List<ExpiresDifferent> fillList(String exchange, String baseAssetName) {
        return new ArrayList<>();
    }

    private void displayExpiresDiffDetails(List<ExpiresDifferent> expiresDifference, String exchange, String baseAssetName) {
    }

    public JPanel getMainPanel() {return mainPanel;}

    //////////////////



    private void setListeners() {
    }

    public static class ExpiresDifferent implements Comparable<ExpiresDifferent> {
        String tradableName;
        String tradableId;
        int psNet;
        int dcNet;

        public ExpiresDifferent(String tradableName, String tradableId, int psNet, int dcNet) {
            this.tradableName = tradableName;
            this.tradableId = tradableId;
            this.psNet = psNet;
            this.dcNet = dcNet;
        }

        public int getDcNet() {
            return dcNet;
        }

        public int getPsNet() {
            return psNet;
        }

        public String getTradableName() {
            return tradableName;
        }

        public String getTradableId() {
            return tradableId;
        }

        @Override
        public int compareTo(@NotNull ExpiresDifferent o) {
            if (tradableName == null || tradableName.equals("") || o.tradableName == null || o.tradableName.equals(""))
                return tradableId.compareTo(o.tradableId);
            String substring1 = tradableName.substring(tradableName.length() - 2);
            String substring2 = o.tradableName.substring(o.tradableName.length() - 2);
            String substring3 = tradableName.substring(tradableName.length() - 5, tradableName.length() - 2);
            String substring4 = o.tradableName.substring(o.tradableName.length() - 5, o.tradableName.length() - 2);
            if (NumberUtils.isDigits(substring2) && NumberUtils.isDigits(substring1)) {
                return Integer.parseInt(substring1) != Integer.parseInt(substring2) ? Integer.parseInt(substring1) - Integer.parseInt(substring2) :
                        Arrays.stream(Month.values()).filter(m -> m.getDisplayName(TextStyle.FULL, Locale.US).substring(0, 3).equalsIgnoreCase(substring3)).findFirst().get()
                                .compareTo(Arrays.stream(Month.values()).filter(m -> m.getDisplayName(TextStyle.FULL, Locale.US).substring(0, 3).equalsIgnoreCase(substring4)).findFirst().get());
            }
            return tradableId.compareTo(o.tradableId);
        }

        @Override
        public String toString() {
            return "ExpiresDifferent{" +
                    "tradableName='" + tradableName + '\'' +
                    ", tradableId='" + tradableId + '\'' +
                    ", psNet=" + psNet +
                    ", dcNet=" + dcNet +
                    '}';
        }
    }
}
