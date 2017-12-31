import org.apache.commons.net.ftp.FTPFile;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ButtonColumn extends AbstractCellEditor implements
        TableCellRenderer, TableCellEditor, ActionListener {
    private JTable table;
    private JButton renderButton;
    private JButton editButton;
    private String text;

    public ButtonColumn(JTable table, int column) {
        super();
        this.table = table;
        renderButton = new JButton();
        editButton = new JButton();
        editButton.setFocusPainted(false);
        editButton.addActionListener(this);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer(this);
        columnModel.getColumn(column).setCellEditor(this);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (hasFocus) {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background"));
        } else if (isSelected) {
            renderButton.setForeground(table.getSelectionForeground());
            renderButton.setBackground(table.getSelectionBackground());
        } else {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background"));
        }

        renderButton.setText((value == null) ? " " : value.toString());
        return renderButton;
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        text = (value == null) ? " " : value.toString();
        editButton.setText(text);
        return editButton;
    }

    public Object getCellEditorValue() {
        return text;
    }

    public void actionPerformed(ActionEvent e) {
        fireEditingStopped();

        FTPFile[]  file1= new FTPFile[0];
        try {
            file1 = Frame_Main.getFile(); //得到所有的文件

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String from_file_name=file1[table.getSelectedRow()].getName();
        int result = 0;
        File file = null;
        String path = null;
        JFileChooser fileChooser = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        fsv.createFileObject(from_file_name);
        //System.out.println(fsv.getHomeDirectory());  
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //fileChooser.setCurrentDirectory(new File(from_file_name));  
        fileChooser.setDialogTitle("另存为:");
        //fileChooser.setApproveButtonText("保存");   
        result = fileChooser.showSaveDialog(null);
        if (JFileChooser.APPROVE_OPTION == result) {
            path=fileChooser.getSelectedFile().getPath()+"\\"; //加"\\"是为了防止在桌面的时候C:destop最后没有\ 
            System.out.println("path: "+path);
            System.out.println("from_file_name:"+from_file_name);
            try {
                Frame_Main.getFtp().download(from_file_name, path);
                System.out.println("下载成功! ");

            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            finally{

//                Frame_Main.getFtp().close_connection();
            }
        }

    }

}