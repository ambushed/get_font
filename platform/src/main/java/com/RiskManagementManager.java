package com;
import com.ui.ProductsVolumeUI;
import javax.swing.*;
import java.awt.*;

public class RiskManagementManager {

    public static void main(String[] args) {

        final JTabbedPane tabbedPane = new JTabbedPane();
        final Point point = new Point(100, 50);

        ProductsVolumeManager manager = new ProductsVolumeManager();
        final ProductsVolumeUI derivativesVolumeUI = new ProductsVolumeUI(manager, ProductsVolumeUI.TabType.DERIVATIVES, point);
        tabbedPane.addTab("Derivatives", derivativesVolumeUI.getMainPanel());

        //final ProductsVolumeUI stocksVolumeUI = new ProductsVolumeUI(manager, ProductsVolumeUI.TabType.STOCKS, point);
        //tabbedPane.addTab("Stocks", stocksVolumeUI.getMainPanel());

        //create frame
        JFrame ctFrame = new JFrame("Risk Management");
        ctFrame.setName("Risk Management"); // For window saver

        ctFrame.setContentPane(tabbedPane);

        ctFrame.pack();
        final Dimension dimension = new Dimension(768, 512);
        ctFrame.setBounds(new Rectangle(point, dimension));
        ctFrame.setMaximumSize(dimension);
        ctFrame.setResizable(true);
        ctFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ctFrame.setVisible(true);
    }
}
