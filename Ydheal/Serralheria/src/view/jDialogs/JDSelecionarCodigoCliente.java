package view.jDialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.util.IllegalFormatException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Model.DAO.ConexaoMySQL;
import modelSuplerclasses.Cliente;
import view.jFrames.cadastros.JFCClientes;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("serial")
public class JDSelecionarCodigoCliente extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfnome;
	private JTable table;

	DefaultTableModel modelo = new DefaultTableModel();

	public JDSelecionarCodigoCliente(Cliente c) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				
				String comando1 = "select cliente_id, cliente_nome, cliente_sobrenome  from tbl_cliente order by cliente_id;";

				while (table.getModel().getRowCount() > 0) {
					((DefaultTableModel) table.getModel()).removeRow(0);
				}

				Cod(comando1);
				
			}
		});

		Image imgIcon = new ImageIcon(this.getClass().getResource(
				"/16px/cliente.png")).getImage();
		setIconImage(imgIcon);
		setResizable(false);
		setTitle("Selecionar C\u00F3digo");
		setBounds(100, 100, 421, 398);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setLocationRelativeTo(null);

		{
			JLabel lblNomeDoModelo = new JLabel("Nome do cliente:");
			lblNomeDoModelo.setBounds(10, 14, 112, 14);
			contentPanel.add(lblNomeDoModelo);
		}

		tfnome = new JTextField();
		tfnome.setBounds(132, 11, 251, 20);
		contentPanel.add(tfnome);
		tfnome.setColumns(10);

		{
			JButton btnBuscar = new JButton("Buscar");
			Image imgBuscar = new ImageIcon(this.getClass().getResource(
					"/16px/search.png")).getImage();
			btnBuscar.setIcon(new ImageIcon(imgBuscar));
			btnBuscar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					if (tfnome.getText().equals("")) {

						String comando = "select cliente_nome from tbl_cliente order by cliente_id;";

						while (table.getModel().getRowCount() > 0) {
							((DefaultTableModel) table.getModel()).removeRow(0);
						}

						Cod(comando);

					}

					else {

						String comando = "select cliente_id, cliente_nome, cliente_sobrenome from tbl_cliente cliente_nome like '"
								+ tfnome.getText() + "%' order by cliente_id;";

						while (table.getModel().getRowCount() > 0) {
							((DefaultTableModel) table.getModel()).removeRow(0);
						}

						Cod(comando);

					}

				}
			});
			btnBuscar.setBounds(210, 36, 112, 23);
			contentPanel.add(btnBuscar);
		}

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 70, 385, 245);
		contentPanel.add(scrollPane);

		{
			modelo.addColumn("Cod.");
			modelo.addColumn("Nome.");

		}

		table = new JTable(modelo) {

			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		// Impede a reordena��o das colunas.
		table.getTableHeader().setReorderingAllowed(false);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {

					c.setId(Integer.parseInt(String.valueOf(modelo.getValueAt(
							table.getSelectedRow(), 0))));

					c.setNome(String.valueOf(modelo.getValueAt(
							table.getSelectedRow(), 1)));

					JOptionPane.showMessageDialog(null, "Cliente Selecionado");

					dispose();

				}
			}
		});

		table.getColumnModel().getColumn(0).setPreferredWidth(1);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);

		scrollPane.setViewportView(table);

		JButton btnNewButton = new JButton("Novo Cliente");
		Image imgNovoCliente = new ImageIcon(this.getClass().getResource(
				"/16px/cliente.png")).getImage();
		btnNewButton.setIcon(new ImageIcon(imgNovoCliente));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					JFCClientes frame = new JFCClientes();
					frame.setModal(true);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);

				} catch (NumberFormatException erroNumero) {

					JOptionPane
							.showMessageDialog(
									null,
									"Algum campo foi preenchido incorretamente. \nPor favor, verifique.",
									"Aviso!", JOptionPane.WARNING_MESSAGE);

					erroNumero.printStackTrace();
				} catch (IllegalFormatException erroFormato) {
					JOptionPane
							.showMessageDialog(
									null,
									"Algum campo foi preenchido incorretamente. \nPor favor, verifique.",
									"Aviso!", JOptionPane.ERROR_MESSAGE);
				}

				catch (Exception erro) {
					erro.printStackTrace();
					JOptionPane.showMessageDialog(null, "Falha na consulta");
				}

				while (table.getModel().getRowCount() > 0) {
					((DefaultTableModel) table.getModel()).removeRow(0);
				}

				String comando = "select cliente_id, cliente_nome, cliente_sobrenome from tbl_cliente order by cliente_id;";

				Cod(comando);

			}
		});
		btnNewButton.setBounds(77, 36, 112, 23);
		contentPanel.add(btnNewButton);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Selecionar");
				Image imgOk = new ImageIcon(this.getClass().getResource(
						"/16px/ok.png")).getImage();
				okButton.setIcon(new ImageIcon(imgOk));
				okButton.setToolTipText("Selecionar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						c.setId(Integer.parseInt(String.valueOf(modelo
								.getValueAt(table.getSelectedRow(), 0))));

						c.setNome(String.valueOf(modelo.getValueAt(
								table.getSelectedRow(), 1)));

						JOptionPane.showMessageDialog(null,
								"Cliente Selecionado");

						dispose();

					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				Image imgCancelar = new ImageIcon(this.getClass().getResource(
						"/16px/cancelar.png")).getImage();
				cancelButton.setIcon(new ImageIcon(imgCancelar));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);

			}
		}
	}

	public void Cod(String comando) {

		try {

			// cria a conecx�o
			java.sql.Connection con = ConexaoMySQL.getConexaoMySQL();

			// n�o sei oq isso faz kk
			java.sql.Statement st = con.createStatement();

			ResultSet resultSet = st.executeQuery(comando);

			while (resultSet.next()) {

				String id = resultSet.getString("cliente_id");

				String nome = resultSet.getString("cliente_nome");

				String sobrenome = resultSet.getString("cliente_sobrenome");

				modelo.addRow(new Object[] { id, nome + " " + sobrenome });

			}

			st.close();
			con.close();

		}// fim do try
		catch (NumberFormatException erroNumero) {

			JOptionPane
					.showMessageDialog(
							null,
							"Algum campo foi preenchido incorretamente. \nPor favor, verifique.",
							"Aviso!", JOptionPane.WARNING_MESSAGE);

			erroNumero.printStackTrace();
		} catch (IllegalFormatException erroFormato) {
			JOptionPane
					.showMessageDialog(
							null,
							"Algum campo foi preenchido incorretamente. \nPor favor, verifique.",
							"Aviso!", JOptionPane.ERROR_MESSAGE);
		}

		catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Falha na consulta");
		}

	}
}
