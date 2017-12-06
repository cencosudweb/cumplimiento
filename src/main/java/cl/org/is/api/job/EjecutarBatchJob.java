package cl.org.is.api.job;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class EjecutarBatchJob {
	final static Logger logger = Logger.getLogger(EjecutarBatchJob.class);
	
	private static BufferedWriter bw;
	private static String path;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Map <String, String> mapArguments = new HashMap<String, String>();
		String sKeyAux = null;

		for (int i = 0; i < args.length; i++) {

			if (i % 2 == 0) {

				sKeyAux = args[i];
			}
			else {

				mapArguments.put(sKeyAux, args[i]);
			}
		}

		try {

			File info              = null;
			File miDir             = new File(".");
			path                   =  miDir.getCanonicalPath();
			info                   = new File(path+"/info.txt");
			bw = new BufferedWriter(new FileWriter(info));
			info("El programa se esta ejecutando...");
			crearTxt(mapArguments);
			System.out.println("El programa finalizo.");
			info("El programa finalizo.");
			bw.close();

		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * Metodo que tiene toda la logica de negocio crea archivo e inserta en la tabla cumplimiento
	 * 
	 * 
	 */
	private static void crearTxt(Map <String, String> mapArguments) {

		Connection dbconnection = crearConexion();
		Connection dbconnection2 = crearConexion2();
		File file1              = null;
		BufferedWriter bw       = null;
		PreparedStatement pstmt = null;
		StringBuffer sb         = null;
		
		

		try {

			Date now = new Date();
			SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
			String currentDate = ft.format(now);
			file1 = new File(path + "/cumplimiento-" + currentDate + ".txt");
			
			Thread.sleep(60);
			info("Pausa para eliminar  CUMPLIMIENTO sleep(60 seg)");
			elimnarCumplimiento(dbconnection2,"DELETE FROM CUMPLIMIENTO C1 where 1 = 1 AND C1.FECHA_COMPROMISO_EOM >= REPLACE(TO_CHAR(to_date('"+currentDate+"', 'YYYY-MM-DD')-8, 'YYYY/MM/DD'),'/','-') AND C1.FECHA_COMPROMISO_EOM <= '"+currentDate+"'");
			commit(dbconnection2,"COMMIT"); 
			
			Thread.sleep(60);
			info("Pausa para eliminar  CUMPLIMIENTO_RESPALDO sleep(60 seg)");
			elimnarCumplimiento(dbconnection2,	" DELETE  FROM CUMPLIMIENTO_RESPALDO CU WHERE 1 = 1 AND CU.FECHA_COMPROMISO_EOM >= REPLACE(TO_CHAR(to_date('"+currentDate+"', 'YYYY-MM-DD')-8, 'YYYY/MM/DD'),'/','-') AND CU.FECHA_COMPROMISO_EOM <= '"+currentDate+"'");
			
			
			Thread.sleep(60);
			info("Pausa para eliminar  CUMPLIMIENTO_KPIWEB sleep(60 seg)");
			elimnarCumplimientoKpi(dbconnection2,	" DELETE  FROM CUMPLIMIENTO_KPIWEB CU WHERE 1 = 1 AND CU.FECHA_COMPROMISO_EOM >= REPLACE(TO_CHAR(to_date('"+currentDate+"', 'YYYY-MM-DD')-8, 'YYYY/MM/DD'),'/','-') AND CU.FECHA_COMPROMISO_EOM <= '"+currentDate+"'");
			
			
			
			
			


			
			
			Thread.sleep(60);
			info("Pausa para llamar  SYSTEM_PKG_CUMPLIMIENTO.SYSTEM_ACTUALIZA_MODELO  sleep(60 seg)");
			CallableStatement cst = dbconnection.prepareCall("{call SYSTEM_PKG_CUMPLIMIENTO.SYSTEM_ACTUALIZA_MODELO ()}");
			cst.execute();
			logger.info("cst="+cst);
			
			//CallableStatement cst2 = dbconnection.prepareCall("{call SYSTEM_PKG_CUMPLIMIENTO.SYSTEM_CARGAR_CUMPLIMIENTO (?)}");
			//CallableStatement cst2 = dbconnection.prepareCall("{call SYSTEM_PKG_CUMPLIMIENTO_V.SYSTEM_CARGAR_CUMPLIMIENTO_V (?)}");
			//CallableStatement cst2 = dbconnection.prepareCall("{call SYSTEM_PKG_CUMPLIMIENTO_L.SYSTEM_CARGAR_CUMPLIMIENTO (?)}");
			Thread.sleep(60);
			info("Pausa para llamar  SYSTEM_PKG_CUMPLIMIENTO.SYSTEM_CARGAR_CUMPLIMIENTO  sleep(60 seg)");
			CallableStatement cst2 = dbconnection.prepareCall("{call SYSTEM_PKG_CUMPLIMIENTO.SYSTEM_CARGAR_CUMPLIMIENTO (?)}");
			cst2.setInt(1, 1);
			cst2.execute();
			//dbconnection.commit();
			//commit(dbconnection,"COMMIT");
			logger.info("cst2="+cst2);
			
			//CallableStatement cst3 = dbconnection.prepareCall("{call SYSTEM_PKG_CARGAR_COMBO.SYSTEM_CARGAR_COMBO ()}");
			//cst3.execute();
			//logger.info("cst3="+cst3);
			
			
			Thread.sleep(90);
			info("Pausa para llamar  SYSTEM_PKG_KPI.SYSTEM_ACTUALIZA_CU_GUIA  sleep(60 seg)");
			CallableStatement cst3 = dbconnection2.prepareCall("{call SYSTEM_PKG_KPI.SYSTEM_ACTUALIZA_CU_GUIA ()}");
			cst3.execute();
			logger.info("cst3="+cst3);
			
			
			Thread.sleep(90);
			info("Pausa para llamar  SYSTEM_PKG_KPI.SYSTEM_ACTUALIZA_MODELO_KPI  sleep(60 seg)");
			CallableStatement cst4 = dbconnection2.prepareCall("{call SYSTEM_PKG_KPI.SYSTEM_ACTUALIZA_MODELO_KPI ()}");
			cst4.execute();
			logger.info("cst4="+cst4);
			
			
			
			
			Thread.sleep(90);
			info("Pausa para llamar  SYSTEM_PKG_KPI.SYSTEM_DEL_MOD_KPI_EST  sleep(60 seg)");
			CallableStatement cst11 = dbconnection2.prepareCall("{call SYSTEM_PKG_KPI.SYSTEM_DEL_MOD_KPI_EST ()}");
			cst11.execute();
			logger.info("cst11="+cst11);
			
			
			Thread.sleep(90);
			info("Pausa para llamar  SYSTEM_PKG_KPI.SYSTEM_DEL_MOD_KPI_ES  sleep(60 seg)");
			CallableStatement cst12 = dbconnection2.prepareCall("{call SYSTEM_PKG_KPI.SYSTEM_DEL_MOD_KPI_ES ()}");
			cst12.execute();
			logger.info("cst12="+cst12);
			
			Thread.sleep(90);
			info("Pausa para llamar  SYSTEM_PKG_KPI.SYSTEM_ACTUALIZA_MODELO_KPI_ES  sleep(60 seg)");
			CallableStatement cst13 = dbconnection2.prepareCall("{call SYSTEM_PKG_KPI.SYSTEM_ACTUALIZA_MODELO_KPI_ES ()}");
			cst13.execute();
			logger.info("cst13="+cst13);
			
			
			
			
			
			
			
			
			String sql = "Insert into CUMPLIMIENTO_RESPALDO (LLAVE,TC_PO_LINE_ID,CUMPL_CALC,CUMPL_EOM,CUMPL_BTK,FECHA_COMPROMISO_EOM,FECHA_COMPROMISO_BTK,ESTADO_CALCE_EOM,SUBESTADO_CALCE_EOM,FECHA_CALCE_EOM,HORA_CALCE_EOM,ESTADO_CALCE_BTK,SUBESTADO_CALCE_BTK,FECHA_CALCE_BTK,HORA_CALCE_BTK,CRUCE_ESTADO,CRUCE_SUB_ESTADO,N_SOLICITUD_CLIENTE,N_ORDEN_DISTRIBU,FECHA_CREACION_ORDEN,EST_ORDEN,ESTADO_DE_LINEA,SKU,CANT_DESC_SKU,LOCAL_VENTA,DEPART,BODEGADESP,RUTCLIENTE,NOMBRECLIENTE,APELLIDOCLIENTE,DIRECCION_CLIENTE,COD_COMUNA,COMUNA,CIUDAD,REGION,HORARIO,TIPO_ORDEN,TIPO_VENTA,O_FACILITY_ALIAS_ID,REGIONENTREGA,TIPO_DE_ORDEN,GUIA,RUT_EMP,DESC_EMP,TIPOGUI,CLIENTE_RETIRA,FECHA_PRIMER_INTENTO,NUMERO_INTENTOS,FECHA_CREA_PKT,HORA_CREA_PKT,NRO_OLA,FECHA_INICIO_OLA,HORA_INICIO_OLA,FECHA_TERMINO_OLA,HORA_TERMINO_OLA,FECHA_CIERRE_CARGA,HORA_CIERRE_CARGA,FECEMIGUI,LEAD_TIME_TRANSPORTE,DIA_SEMANA_ETA,ENTREGA_DIA_COMPROMISO,VENTA_EMPRESA,CUMPLE,CUMPLIMIENTO_2,CUMPLE_RESUMEN,BODEGA_DESPACHO,DIF_ETA_CARGA,FECHA_CARGA_REQUERIDA,EVAL_SISTEMA,EVAL_CD,EVAL_1ER_INTENTO,CUMPLIMIENTO_CLIENTE,RESPONSABLE,TIPO_CUMPLIMIENTO, FECHA_INGRESO) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement pstmt1 = dbconnection2.prepareStatement(sql);
			logger.info("Sql Insert"+sql);
			
			
			//CallableStatement cst3 = dbconnection2.prepareCall("{call SYSTEM_PKG_CUMPLIMIENTO_V.SYSTEM_ACTUALIZA_MODELO_V ()}");
			//cst3.execute();
			//logger.info("cst3="+cst3);
			
		
			
			
			
			
			sb = new StringBuffer();
			sb.append("select * from VISTA_CUMPLIMIENTO");
			logger.info("Sql "+sb);
			
			pstmt = dbconnection.prepareStatement(sb.toString());
			sb = new StringBuffer();
			ResultSet rs = pstmt.executeQuery();
			
			bw = new BufferedWriter(new FileWriter(file1));
			bw.write("LLAVE;");
			bw.write("TC_PO_LINE_ID;");
			bw.write("CUMPL_CALC;");
			bw.write("CUMPL_EOM;");
			bw.write("CUMPL_BTK;");
			bw.write("FECHA_COMPROMISO_EOM;");
			bw.write("FECHA_COMPROMISO_BTK;");
			bw.write("ESTADO_CALCE_EOM;");
			bw.write("SUBESTADO_CALCE_EOM;");
			bw.write("FECHA_CALCE_EOM;");
			bw.write("HORA_CALCE_EOM;");
			bw.write("ESTADO_CALCE_BTK;");
			bw.write("SUBESTADO_CALCE_BTK;");
			bw.write("FECHA_CALCE_BTK;");
			bw.write("HORA_CALCE_BTK;");
			bw.write("CRUCE_ESTADO;");
			bw.write("CRUCE_SUB_ESTADO;");
			bw.write("N_SOLICITUD_CLIENTE;");
			bw.write("N_ORDEN_DISTRIBU;");
			bw.write("FECHA_CREACION_ORDEN;");
			bw.write("EST_ORDEN;");
			bw.write("ESTADO_DE_LINEA;");
			bw.write("SKU;");
			bw.write("CANT_DESC_SKU;");
			bw.write("LOCAL_VENTA;");
			bw.write("DEPART;");
			bw.write("BODEGADESP;");
			bw.write("RUTCLIENTE;");
			bw.write("NOMBRECLIENTE;");
			bw.write("APELLIDOCLIENTE;");
			bw.write("DIRECCION_CLIENTE;");
			bw.write("COD_COMUNA;");
			bw.write("COMUNA;");
			bw.write("CIUDAD;");
			bw.write("REGION;");
			bw.write("HORARIO;");
			bw.write("TIPO_ORDEN;");
			bw.write("TIPO_VENTA;");
			bw.write("O_FACILITY_ALIAS_ID;");
			bw.write("REGIONENTREGA;");
			bw.write("TIPO_DE_ORDEN;");
			bw.write("GUIA;");
			bw.write("RUT_EMP;");
			bw.write("DESC_EMP;");
			bw.write("TIPOGUI;");
			bw.write("CLIENTE_RETIRA;");
			bw.write("FECHA_PRIMER_INTENTO;");
			bw.write("NUMERO_INTENTOS;");
			bw.write("FECHA_CREA_PKT;");
			bw.write("HORA_CREA_PKT;");
			bw.write("NRO_OLA;");
			bw.write("FECHA_INICIO_OLA;");
			bw.write("HORA_INICIO_OLA;");
			bw.write("FECHA_TERMINO_OLA;");
			bw.write("HORA_TERMINO_OLA;");
			bw.write("FECHA_CIERRE_CARGA;");
			bw.write("HORA_CIERRE_CARGA;");
			bw.write("FECEMIGUI;");
			bw.write("LEAD_TIME_TRANSPORTE;");
			bw.write("DIA_SEMANA_ETA;");
			bw.write("ENTREGA_DIA_COMPROMISO;");
			bw.write("VENTA_EMPRESA;");
			bw.write("CUMPLE;");
			bw.write("CUMPLIMIENTO_2;");
			bw.write("CUMPLE_RESUMEN;");
			bw.write("BODEGA_DESPACHO;");
			bw.write("DIF_ETA_CARGA;");
			bw.write("FECHA_CARGA_REQUERIDA;");
			bw.write("EVAL_SISTEMA;");
			bw.write("EVAL_CD;");
			bw.write("EVAL_1ER_INTENTO;");
			bw.write("CUMPLIMIENTO_CLIENTE;");
			bw.write("RESPONSABLE;");
			bw.write("TIPO_CUMPLIMIENTO;");
			bw.write("FECHA_INGRESO;\n");
			while (rs.next()) {
				String cumplimientoFormula = obtenerFormulaCumplimiento(rs.getString("CUMPL_CALC"), rs.getString("CUMPL_BTK"), rs.getString("CUMPL_EOM"), rs.getString("CRUCE_SUB_ESTADO"), rs.getString("CRUCE_ESTADO"), rs.getString("ESTADO_DE_LINEA"), rs.getString("ESTADO_CALCE_BTK"), rs.getString("GUIA"));
				String cumple = obtenerFormulaCumple(cumplimientoFormula);
				String cumpleDos = obtenerFormulaCumpleDos(cumplimientoFormula, rs.getString("ESTADO_DE_LINEA"), rs.getString("EST_ORDEN"));
				String cumpleResumen = obtenerCumpleResumen(cumplimientoFormula);
				String bodegaDespacho = obtenerBodegaDespacho(rs.getString("O_FACILITY_ALIAS_ID"));
				Integer difEtaCarga = obtenerFormulaDifEtaCarga((rs.getString("FECHA_COMPROMISO_EOM") != null ? rs.getString("FECHA_COMPROMISO_EOM") : "0000-00-00"), (rs.getString("FECHA_CIERRE_CARGA") != null ? rs.getString("FECHA_CIERRE_CARGA") : "0000-00-00"));
				String fechaCargaRequerida = obtenerFormulaFechaCargaRequerida((rs.getString("FECHA_COMPROMISO_EOM") != null ? rs.getString("FECHA_COMPROMISO_EOM") : "0000-00-00"), rs.getString("LEAD_TIME_TRANSPORTE"));
				String evolSistema = obtenerFormulaEvolucionSistema(cumpleDos, (rs.getString("ENTREGA_DIA_COMPROMISO") != null ? rs.getString("ENTREGA_DIA_COMPROMISO") : ""));
				String evolCd = obtenerFormulaEvolucionaCd((rs.getString("FECHA_CIERRE_CARGA") != null ? rs.getString("FECHA_CIERRE_CARGA") : ""), rs.getString("LEAD_TIME_TRANSPORTE"), fechaCargaRequerida);
				String primerIntento = obtenerFormulaEvolucionPrimerIntento( (rs.getString("FECHA_PRIMER_INTENTO") != null ? rs.getString("FECHA_PRIMER_INTENTO") : ""), (rs.getString("FECHA_COMPROMISO_EOM") != null ? rs.getString("FECHA_COMPROMISO_EOM") : ""));
				String cumplimientoCliente = obtenerFormulaCumplimientoCliente(cumplimientoFormula, (rs.getString("FECHA_CALCE_EOM") != null ? rs.getString("FECHA_CALCE_EOM") : ""), (rs.getString("FECHA_CALCE_BTK") != null ? rs.getString("FECHA_CALCE_BTK") : ""), (rs.getString("FECHA_COMPROMISO_EOM") != null ? rs.getString("FECHA_COMPROMISO_EOM"): ""), (rs.getString("FECHA_COMPROMISO_BTK") != null ? rs.getString("FECHA_COMPROMISO_BTK"): ""));
				String responsableCumpl = obtenerFormulaResponsable(cumpleResumen, evolSistema, evolCd, primerIntento, cumplimientoFormula);
				String tipoCumplimiento = obtenerFormulaTipoCumplimiento(cumple);
				java.util.Date date = new java.util.Date();
				long t = date.getTime();
				java.sql.Date sqlDate = new java.sql.Date(t);
				
				bw.write(rs.getString("LLAVE")+ ";");
				bw.write(rs.getString("TC_PO_LINE_ID")+ ";");
				bw.write(rs.getString("CUMPL_CALC")+ ";");
				bw.write(rs.getString("CUMPL_EOM")+";");
				bw.write(rs.getString("CUMPL_BTK") + ";");
				bw.write(rs.getString("FECHA_COMPROMISO_EOM")+ ";");
				bw.write(rs.getString("FECHA_COMPROMISO_BTK") + ";");
				bw.write(rs.getString("ESTADO_CALCE_EOM") + ";");
				bw.write(rs.getString("SUBESTADO_CALCE_EOM") + ";");
				bw.write(rs.getString("FECHA_CALCE_EOM") + ";");
				bw.write(rs.getString("HORA_CALCE_EOM") + ";");
				bw.write(rs.getString("ESTADO_CALCE_BTK") + ";");
				bw.write(rs.getString("SUBESTADO_CALCE_BTK") + ";");
				bw.write(rs.getString("FECHA_CALCE_BTK") + ";");
				bw.write(rs.getString("HORA_CALCE_BTK") + ";");
				bw.write(rs.getString("CRUCE_ESTADO") + ";");
				bw.write(rs.getString("CRUCE_SUB_ESTADO") + ";");
				bw.write(rs.getString("N_SOLICITUD_CLIENTE") + ";");
				bw.write(rs.getString("N_ORDEN_DISTRIBU") + ";");
				bw.write(rs.getString("FECHA_CREACION_ORDEN") + ";");
				bw.write(rs.getString("EST_ORDEN")+ ";");
				bw.write(rs.getString("ESTADO_DE_LINEA")+ ";");
				bw.write(rs.getString("SKU")+ ";");
				bw.write(rs.getString("CANT_DESC_SKU")+ ";");
				bw.write(rs.getString("LOCAL_VENTA")+ ";");
				bw.write(rs.getString("DEPART")+ ";");
				bw.write(rs.getString("BODEGADESP")+ ";");
				bw.write(rs.getString("RUTCLIENTE")+ ";");
				bw.write(rs.getString("NOMBRECLIENTE")+ ";");
				bw.write(rs.getString("APELLIDOCLIENTE") + ";");
				bw.write(rs.getString("DIRECCION_CLIENTE") + ";");
				bw.write(rs.getString("COD_COMUNA")+ ";");
				bw.write(rs.getString("COMUNA")+ ";");
				bw.write(rs.getString("CIUDAD")+ ";");
				bw.write(rs.getString("REGION")+ ";");
				bw.write(rs.getString("HORARIO")+ ";");
				bw.write(rs.getString("TIPO_ORDEN")+ ";");
				bw.write(rs.getString("TIPO_VENTA")+ ";");
				bw.write(rs.getString("O_FACILITY_ALIAS_ID")+ ";");
				bw.write(rs.getString("REGIONENTREGA")+ ";");
				bw.write(rs.getString("TIPO_DE_ORDEN")+ ";");
				bw.write(rs.getString("GUIA")+ ";");
				bw.write(rs.getString("RUT_EMP")+ ";");
				bw.write(rs.getString("DESC_EMP") + ";");
				bw.write(rs.getString("TIPOGUI") + ";");
				bw.write(rs.getString("CLIENTE_RETIRA") + ";");
				bw.write(rs.getString("FECHA_PRIMER_INTENTO")+ ";");
				bw.write(rs.getString("NUMERO_INTENTOS") + ";");
				bw.write(rs.getString("FECHA_CREA_PKT") + ";");
				bw.write(rs.getString("HORA_CREA_PKT") + ";");
				bw.write(rs.getString("NRO_OLA") + ";");
				bw.write(rs.getString("FECHA_INICIO_OLA")+ ";");
				bw.write(rs.getString("HORA_INICIO_OLA")+ ";");
				bw.write(rs.getString("FECHA_TERMINO_OLA")+ ";");
				bw.write(rs.getString("HORA_TERMINO_OLA")+ ";");
				bw.write(rs.getString("FECHA_CIERRE_CARGA")+ ";");
				bw.write(rs.getString("HORA_CIERRE_CARGA")+ ";");
				bw.write(rs.getString("FECEMIGUI") + ";");
				bw.write(rs.getString("LEAD_TIME_TRANSPORTE")+ ";");
				bw.write(rs.getString("DIA_SEMANA_ETA")+ ";");
				bw.write(rs.getString("ENTREGA_DIA_COMPROMISO")+ ";");
				bw.write(rs.getString("VENTA_EMPRESA")+ ";");
				bw.write(cumple+ ";");
				bw.write(cumpleDos+ ";");
				bw.write(cumpleResumen+ ";");
				bw.write(bodegaDespacho+ ";");
				bw.write(String.valueOf(difEtaCarga) + ";");
				bw.write(fechaCargaRequerida + ";");
				bw.write(evolSistema + ";");
				bw.write(evolCd + ";");
				bw.write(primerIntento + ";");
				bw.write(cumplimientoCliente+ ";");
				bw.write(responsableCumpl+ ";");
				bw.write(tipoCumplimiento+ ";");
				bw.write(sqlDate + "\n");
				
				
				pstmt1.setString(1, rs.getString("LLAVE") != null ? rs.getString("LLAVE") : "");
				pstmt1.setString(2, rs.getString("TC_PO_LINE_ID") != null ? rs.getString("TC_PO_LINE_ID") : "");
				pstmt1.setString(3, rs.getString("CUMPL_CALC") != null ? rs.getString("CUMPL_CALC") : "");
				pstmt1.setString(4, rs.getString("CUMPL_EOM") != null ? rs.getString("CUMPL_EOM") : "");
				pstmt1.setString(5, rs.getString("CUMPL_BTK") != null ? rs.getString("CUMPL_BTK") : "");
				pstmt1.setString(6, rs.getString("FECHA_COMPROMISO_EOM") != null ? rs.getString("FECHA_COMPROMISO_EOM") : "");
				pstmt1.setString(7, rs.getString("FECHA_COMPROMISO_BTK") != null ? rs.getString("FECHA_COMPROMISO_BTK") : "");
				pstmt1.setString(8, rs.getString("ESTADO_CALCE_EOM") != null ? rs.getString("ESTADO_CALCE_EOM") : "");
				pstmt1.setString(9, rs.getString("SUBESTADO_CALCE_EOM") != null ? rs.getString("SUBESTADO_CALCE_EOM") : "");
				pstmt1.setString(10, rs.getString("FECHA_CALCE_EOM") != null ? rs.getString("FECHA_CALCE_EOM") : "");
				pstmt1.setString(11, rs.getString("HORA_CALCE_EOM") != null ? rs.getString("HORA_CALCE_EOM") : "");
				pstmt1.setString(12, rs.getString("ESTADO_CALCE_BTK") != null ? rs.getString("ESTADO_CALCE_BTK") : "");
				pstmt1.setString(13, rs.getString("SUBESTADO_CALCE_BTK") != null ? rs.getString("SUBESTADO_CALCE_BTK") : "");
				pstmt1.setString(14, rs.getString("FECHA_CALCE_BTK") != null ? rs.getString("FECHA_CALCE_BTK") : "");
				pstmt1.setString(15, rs.getString("HORA_CALCE_BTK") != null ? rs.getString("HORA_CALCE_BTK") : "");
				pstmt1.setString(16, rs.getString("CRUCE_ESTADO") != null ? rs.getString("CRUCE_ESTADO") : "");
				pstmt1.setString(17, rs.getString("CRUCE_SUB_ESTADO") != null ? rs.getString("CRUCE_SUB_ESTADO") : "");
				pstmt1.setString(18, rs.getString("N_SOLICITUD_CLIENTE") != null ? rs.getString("N_SOLICITUD_CLIENTE") : "");
				pstmt1.setString(19, rs.getString("N_ORDEN_DISTRIBU") != null ? rs.getString("N_ORDEN_DISTRIBU") : "");
				pstmt1.setString(20, rs.getString("FECHA_CREACION_ORDEN") != null ? rs.getString("FECHA_CREACION_ORDEN") : "");
				pstmt1.setString(21, rs.getString("EST_ORDEN") != null ? rs.getString("EST_ORDEN") : "");
				pstmt1.setString(22, rs.getString("ESTADO_DE_LINEA") != null ? rs.getString("ESTADO_DE_LINEA") : "");
				pstmt1.setString(23, rs.getString("SKU") != null ? rs.getString("SKU") : "");
				pstmt1.setString(24, rs.getString("CANT_DESC_SKU") != null ? rs.getString("CANT_DESC_SKU") : "");
				pstmt1.setString(25, rs.getString("LOCAL_VENTA") != null ? rs.getString("LOCAL_VENTA") : "");
				pstmt1.setString(26, rs.getString("DEPART") != null ? rs.getString("DEPART") : "");
				pstmt1.setString(27, rs.getString("BODEGADESP") != null ? rs.getString("BODEGADESP") : "");
				pstmt1.setString(28, rs.getString("RUTCLIENTE") != null ? rs.getString("RUTCLIENTE") : "");
				pstmt1.setString(29, rs.getString("NOMBRECLIENTE") != null ? rs.getString("NOMBRECLIENTE") : "");
				pstmt1.setString(30, rs.getString("APELLIDOCLIENTE") != null ? rs.getString("APELLIDOCLIENTE") : "");
				pstmt1.setString(31, rs.getString("DIRECCION_CLIENTE") != null ? rs.getString("DIRECCION_CLIENTE") : "");
				pstmt1.setString(32, rs.getString("COD_COMUNA") != null ? rs.getString("COD_COMUNA") : "");
				pstmt1.setString(33, rs.getString("COMUNA") != null ? rs.getString("COMUNA") : "");
				pstmt1.setString(34, rs.getString("CIUDAD") != null ? rs.getString("CIUDAD") : "");
				pstmt1.setString(35, rs.getString("REGION") != null ? rs.getString("REGION") : "");
				pstmt1.setString(36, rs.getString("HORARIO") != null ? rs.getString("HORARIO") : "-");
				pstmt1.setString(37, rs.getString("TIPO_ORDEN") != null ? rs.getString("TIPO_ORDEN") : "-");
				pstmt1.setString(38, rs.getString("TIPO_VENTA") != null ? rs.getString("TIPO_VENTA") : "-");
				pstmt1.setString(39, rs.getString("O_FACILITY_ALIAS_ID") != null ? rs.getString("O_FACILITY_ALIAS_ID") : "");
				pstmt1.setString(40, rs.getString("REGIONENTREGA") != null ? rs.getString("REGIONENTREGA") : "");
				pstmt1.setString(41, rs.getString("TIPO_DE_ORDEN") != null ? rs.getString("TIPO_DE_ORDEN") : "");
				pstmt1.setString(42, rs.getString("GUIA") != null ? rs.getString("GUIA") : "");
				pstmt1.setString(43, rs.getString("RUT_EMP") != null ? rs.getString("RUT_EMP") : "");
				pstmt1.setString(44, rs.getString("DESC_EMP") != null ? rs.getString("DESC_EMP") : "-");
				pstmt1.setString(45, rs.getString("TIPOGUI") != null ? rs.getString("TIPOGUI") : "-");
				pstmt1.setString(46, rs.getString("CLIENTE_RETIRA") != null ? rs.getString("CLIENTE_RETIRA") : "-");
				pstmt1.setString(47, rs.getString("FECHA_PRIMER_INTENTO") != null ? rs.getString("FECHA_PRIMER_INTENTO") : "");
				pstmt1.setString(48, rs.getString("NUMERO_INTENTOS") != null ? rs.getString("NUMERO_INTENTOS") : "");
				pstmt1.setString(49, rs.getString("FECHA_CREA_PKT") != null ? rs.getString("FECHA_CREA_PKT") : "");
				pstmt1.setString(50, rs.getString("HORA_CREA_PKT") != null ? rs.getString("HORA_CREA_PKT") : "");
				pstmt1.setString(51, rs.getString("NRO_OLA") != null ? rs.getString("NRO_OLA") : "");
				pstmt1.setString(52, rs.getString("FECHA_INICIO_OLA") != null ? rs.getString("FECHA_INICIO_OLA") : "");
				pstmt1.setString(53, rs.getString("HORA_INICIO_OLA") != null ? rs.getString("HORA_INICIO_OLA") : "");
				pstmt1.setString(54, rs.getString("FECHA_TERMINO_OLA") != null ? rs.getString("FECHA_TERMINO_OLA") : "");
				pstmt1.setString(55, rs.getString("HORA_TERMINO_OLA") != null ? rs.getString("HORA_TERMINO_OLA") : "");
				pstmt1.setString(56, rs.getString("FECHA_CIERRE_CARGA") != null ? rs.getString("FECHA_CIERRE_CARGA") : "");
				pstmt1.setString(57, rs.getString("HORA_CIERRE_CARGA") != null ? rs.getString("HORA_CIERRE_CARGA") : "");
				pstmt1.setString(58, rs.getString("FECEMIGUI") != null ? rs.getString("FECEMIGUI") : "");
				pstmt1.setString(59, rs.getString("LEAD_TIME_TRANSPORTE") != null ? rs.getString("LEAD_TIME_TRANSPORTE") : "");
				pstmt1.setString(60, rs.getString("DIA_SEMANA_ETA") != null ? rs.getString("DIA_SEMANA_ETA") : "");
				pstmt1.setString(61, rs.getString("ENTREGA_DIA_COMPROMISO") != null ? rs.getString("ENTREGA_DIA_COMPROMISO") : "");
				pstmt1.setString(62, rs.getString("VENTA_EMPRESA") != null ? rs.getString("VENTA_EMPRESA") : "-");
				pstmt1.setString(63, cumple != null ? cumple : "-");
				pstmt1.setString(64, cumpleDos != null ? cumpleDos : "-");
				pstmt1.setString(65, cumpleResumen != null ? cumpleResumen : "-");
				pstmt1.setString(66, bodegaDespacho != null ? bodegaDespacho : "-");
				pstmt1.setString(67, String.valueOf(difEtaCarga) != null ? String.valueOf(difEtaCarga) : "-");
				pstmt1.setString(68, fechaCargaRequerida != null ? fechaCargaRequerida : "-");
				pstmt1.setString(69, evolSistema != null ? evolSistema : "-");
				pstmt1.setString(70, evolCd != null ? evolCd : "-");
				pstmt1.setString(71, primerIntento != null ? primerIntento : "-");
				pstmt1.setString(72, !"".equals(cumplimientoCliente) && cumplimientoCliente != null ? cumplimientoCliente : "-");
				pstmt1.setString(73, responsableCumpl != null ? responsableCumpl : "-");
				pstmt1.setString(74, tipoCumplimiento != null ? tipoCumplimiento : "-");
				pstmt1.setDate(75, sqlDate);
				pstmt1.executeUpdate();
				commit(dbconnection2,"COMMIT");
			}
			bw.write(sb.toString());

			//Thread.sleep(60);
			//info("Pausa para eliminar cumplimiento estatico sleep(60 seg)");
			//elimnarCumplimientoEstatico(dbconnection2," DELETE FROM CUMPLIMIENTO_ESTATICO C1 where 1 = 1 AND C1.FECHA_COMPROMISO_EOM >= REPLACE(TO_CHAR(to_date('"+currentDate+"', 'YYYY-MM-DD')-1, 'YYYY/MM/DD'),'/','-') AND C1.FECHA_COMPROMISO_EOM <= '"+currentDate+"'");
			//commit(dbconnection2,"COMMIT"); 
			
			//Thread.sleep(60);
			//info("Pausa para Insertar  cumplimiento estatico sleep(60 seg)");
			//agregarCumplimientoEstatico(dbconnection2,	" INSERT INTO CUMPLIMIENTO_ESTATICO SELECT * FROM CUMPLIMIENTO where 1 = 1 AND FECHA_COMPROMISO_EOM >= REPLACE(TO_CHAR(to_date('"+currentDate+"', 'YYYY-MM-DD')-1, 'YYYY/MM/DD'),'/','-') AND FECHA_COMPROMISO_EOM <= '"+currentDate+"'");
			
			
			//Thread.sleep(60);
			//info("Pausa para Insertar  cumplimiento estatico sleep(60 seg)");
			//elimnarCumplimientoEstatico(dbconnection2,	" DELETE  FROM CUMPLIMIENTO_KPIWEB CU WHERE 1 = 1 AND CU.FECHA_COMPROMISO_EOM >= REPLACE(TO_CHAR(to_date('"+currentDate+"', 'YYYY-MM-DD')-8, 'YYYY/MM/DD'),'/','-') AND CU.FECHA_COMPROMISO_EOM <= '"+currentDate+"'");
			
			
			//Thread.sleep(60);
			//CallableStatement cst4 = dbconnection2.prepareCall("{call SYSTEM_PKG_KPI.SYSTEM_ACTUALIZA_MODELO_KPI ()}");
			//cst4.execute();
			//logger.info("cst4="+cst4);
			
			info("Archivos creados.");
		}
		catch (Exception e) {

			info("[crearTxt1]Exception:"+e.getMessage());
			
		}
		finally {

			cerrarTodo(dbconnection, pstmt, bw);
			cerrarTodo(dbconnection2, null, null);
			
		}
	}

	/**
	 * Metodo que calcula la formula de cumplimiento de ordenes de compra
	 * 
	 * @param String, cumplimiento calidad compra
	 * @param String, cumplimiento beetrack compra
	 * @param String, cumplimiento Eom de compra
	 * @param String, cruce del sub Estado de compra
	 * @param String, cruce estado de compra
	 * @param String, estado de linea de compra
	 * @param String, estado calce beetrak de compra
	 * @param String, numero de guia de compra
	 * @return String indicando el estado de orden de compra
	 * 
	 */
	private static String obtenerFormulaCumplimiento(String cumplCal, String cumplBtk, String cumplEom, String cruceSubEstado,
													 String cruceEstado, String estadoLinea, String estadoCalceBtk, String guia) {
		// TODO Auto-generated method stub
		String result = null;
		if ("".equals(cumplCal) || cumplCal == null) {
			if (("".equals(cumplBtk) || "No Cumplimiento".equals(cumplBtk) || cumplBtk == null)) {
				if ("Sin Informacion".equals(cumplEom)) {
					if (cruceSubEstado == "" || cruceSubEstado == null) {
						if ("".equals(cruceEstado) || cruceEstado == null) {
							if ("Shipped".equals(estadoLinea)) {
								if ("En Proceso de Carga"
										.equals(estadoCalceBtk)) {
									result = estadoCalceBtk;
								} else {
									if ("".equals(guia) || guia == null) {
										result = "Problema Carga Guía";
									} else {
										result = "En Ruta o CT";
									}
								}
							} else {
								result = "Preparación CD";
							}
						} else {
							result = cruceEstado;
						}
					} else {
						result = cruceSubEstado;
					}
				} else {
					result = cumplEom;
				}
			} else {
				result = cumplBtk;
			}
		} else {
			result = cumplCal;
		}
		return result;
	}

	/**
	 * Metodo que calcula si cumple la orden de compra segun cumplimiento
	 * 
	 * @param String, se obtiene desde el metodo obtenerFormulaCumplimiento
	 * @return String indicando si  CUMPLE es adelantado o en fecha, ATRASADO si  es atrasado y si no No cumple
	 */
	private static String obtenerFormulaCumple(String cumplFormula) {
		String result = null;
		if ("ADELANTADO".equals(cumplFormula)) {
			result = "1.- CUMPLE";
		} else {
			if ("EN FECHA".equals(cumplFormula)) {
				result = "1.- CUMPLE";
			} else {
				if ("ATRASADO".equals(cumplFormula)) {
					result = "2.- ATRASADO";
				} else {
					result = "3.- NO CUMPLE";
				}
			}
		}
		return result;
	}

	/**
	 * Metodo que calcula si cumple la orden de compra esto es segun excel
	 * 
	 * @param String, Se obtiene del estado del metodo obtenerFormulaCumplimiento
	 * @param String, estado de linea de orden de compra
	 * @param String, estado de orden de orden de compra
	 * @return String indica si  es Sin Información es igual a cumplimiento si no   por fecto el valor cumpl
	 */
	private static String obtenerFormulaCumpleDos(String cumplFormula, String estadoLinea, String estadoOrden) {
		String result = null;
		if ("Sin Informacion".equals(cumplFormula)) {
			result = "Sin Información ("
					+ ("".equals(estadoLinea) || estadoLinea == null ? estadoOrden
							: estadoLinea) + ")";
		} else {
			result = cumplFormula;
		}
		return result;
	}

	/**
	 * Metodo que calcula la formula de el Resumen de orden de compra
	 * 
	 * @param String, se obtiene desde el metodo obtenerFormulaCumplimiento
	 * @return String indicando el resumen de la orden de compra
	 */
	private static String obtenerCumpleResumen(String cumplFormula) {
		String result = null;
		if ("ATRASADO".equals(cumplFormula)) {
			result = "ATRASADO";
		} else {
			if ("ADELANTADO".equals(cumplFormula)
					|| "EN FECHA".equals(cumplFormula)) {
				result = "CUMPLIMIENTO";
			} else if ("Producto No Corresponde".equals(cumplFormula)
					|| "Preparación CD".equals(cumplFormula)
					|| "Daño Producto".equals(cumplFormula)
					|| "Motivo Transportes".equals(cumplFormula)
					|| "En Proceso de Carga".equals(cumplFormula)
					|| "RechaEXP".equals(cumplFormula)
					|| "En Ruta o CT".equals(cumplFormula)
					|| "Error Sistémico".equals(cumplFormula)) {
				result = "PROBLEMA PARIS";
			} else {
				if ("Motivos Cliente".equals(cumplFormula)
						|| "Expectativa".equals(cumplFormula)
						|| "Cliente No Está".equals(cumplFormula)
						|| "Dirección Errónea".equals(cumplFormula)
						|| "NCporPlazo".equals(cumplFormula)
						|| "Nota de Crédito".equals(cumplFormula)) {
					result = "PROBLEMA CLIENTE";
				} else {
					result = "SIN INFORMACION";
				}
			}
		}
		return result;
	}

	/**
	 * Metodo que entrega el calculo de bodega de despacho
	 * 
	 * @param String, alias de la orden de compra
	 * @return String indica el numero de bodega de despacho
	 */
	private static String obtenerBodegaDespacho(String facilityAlias) {
		// TODO Auto-generated method stub
		String result = null;
		if ("200".equals(facilityAlias) || "012".equals(facilityAlias)
				|| "12".equals(facilityAlias) || "200".equals(facilityAlias)) {
			result = facilityAlias;
		} else {
			result = "Proveedor";
		}

		return result;
	}

	/**
	 * Metodo que calcula diferencia de Eta carga 
	 * 
	 * @param String, fecha de compromiso de orden de compra
	 * @param String, fecha de cierre de carga de orden de compra
	 * @return Integer indica la diferencia de dias de la orden de compra
	 */
	private static Integer obtenerFormulaDifEtaCarga(String fechaCompromiso, String fechaCierreCarga) {
		// TODO Auto-generated method stub
		int result = 0;
		if ("".equals(fechaCompromiso) || fechaCompromiso == null
				&& "".equals(fechaCierreCarga) || fechaCierreCarga == null) {
			result = 19999;
		} else {
			result = fechasDiferenciaEnDias(fechaDesdeString(fechaCompromiso),
					fechaDesdeString(fechaCierreCarga));
		}
		return result;
	}

	/**
	 * Metodo que calcula la fecha de carga requerida de orden de compra
	 * 
	 * @param String, fecha compromiso eom 
	 * @param String, dirige tiempo de transporte 
	 * @return String indica la fecha carga requerida
	 */
	private static String obtenerFormulaFechaCargaRequerida(String fechaCompromisoEom, String leadTimeTransport) {
		// TODO Auto-generated method stub
		String result = null;
		int codigoDiaSemana = 0;

		codigoDiaSemana = diaDeLaSemana(fechaCompromisoEom, leadTimeTransport);

		if (codigoDiaSemana == 1) {
			String restaFecha = restarDiasFecha(fechaCompromisoEom,
					leadTimeTransport);
			result = restarDiasFecha(restaFecha, "1");
		} else {
			result = restarDiasFecha(fechaCompromisoEom, leadTimeTransport);
		}
		return result;
	}

	/**
	 * Metodo calcula la evolucion de la orden de compra
	 * 
	 * @param String, obtiene obtenerFormulaCumpleDos
	 * @param String, fecha de compromiso
	 * @return String indica si cumple o no cumple la evolucion
	 */
	private static String obtenerFormulaEvolucionSistema(String cumpleDos, String entregaDiaCompromiso) {
		// TODO Auto-generated method stub
		String result = null;
		if ("Problema Carga Guía".equals(cumpleDos)) {
			result = "No Cumple";
		} else {
			if ("1".equals(entregaDiaCompromiso)) {
				result = "Cumple";
			} else {
				if ("0".equals(entregaDiaCompromiso)) {
					result = "No Cumple";
				} else {
					result = "Faltan Datos";
				}
			}
		}
		return result;
	}

	/**
	 * Metodo que calcula la evolucion de cd 
	 * 
	 * @param String, fecha de cierre carga
	 * @param String, dirige el tiempo de transporte 
	 * @param String, fecha cierre carga
	 * @return String indica el estado de la evoluciona orden de compra
	 */
	private static String obtenerFormulaEvolucionaCd(String fechaCierreCarga, String leadTimeTrans, String fechaCargaRequerida) {
		// TODO Auto-generated method stub
		String result = null;
		Boolean fechaMayor = false;

		if ("".equals(fechaCierreCarga) || fechaCierreCarga == null
				|| "".equals(leadTimeTrans) || leadTimeTrans == null) {
			result = "Sin información";
		} else {
			try {
				fechaMayor = obtenerFechaMayorIgual(fechaCargaRequerida,
						fechaCierreCarga);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error funcion obtenerFormulaEvolucionaCd fechaMayor "+ e.getMessage());
			}
			if (fechaMayor) {
				result = "Cumple";
			} else {
				result = "No Cumple";
			}
		}
		return result;
	}

	/**
	 * Metodo que obtiene la formula de evolucion de primero intento de orden de compra
	 * 
	 * @param String, fecha primero intento
	 * @param String, fecha compromiso Eom
	 * @return String indica si cumple o no cumple el primer intento de orden de compra
	 */
	private static String obtenerFormulaEvolucionPrimerIntento(String fechaPrimerIntento, String fechaCompromisoEom) {
		// TODO Auto-generated method stub
		String result = null;
		Boolean fechaMenor = false;
		if ("".equals(fechaPrimerIntento) || fechaPrimerIntento == null) {
			result = "No Cumple";
		} else {
			try {
				fechaMenor = obtenerFechaMenorIgual(fechaPrimerIntento,
						fechaCompromisoEom);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (fechaMenor) {
				result = "Cumple";
			} else {
				result = "No Cumple";
			}
		}
		return result;
	}

	/**
	 * Metodo que obtiene la formula de cumplimiento de cliente 
	 * 
	 * @param String, cumple de orden compra
	 * @param String, fecha calce Eom
	 * @param String, fecha calce Beetrak
	 * @param String, fecha compromiso
	 * @param String, fecha compromiso beetrak
	 * @return String indica el si cumple o no el estado del cliente
	 */
	private static String obtenerFormulaCumplimientoCliente(String cumplimientoFormula, String fechaCalceEom, String fechaCalceBtk, 
															String fechaCompromiso, String fechaCompromisoBtk) {
		// TODO Auto-generated method stub
		String result = null;

		if (("ADELANTADO".equals(cumplimientoFormula) || "ATRASADO".equals(cumplimientoFormula) || "EN FECHA".equals(cumplimientoFormula) ) && (fechaCalceEom!="" && fechaCalceBtk!="" && !obtenerFechaIgual(
				fechaCalceEom, fechaCalceBtk))   || (fechaCompromiso!="" && fechaCompromisoBtk!="" && !obtenerFechaIgual(
						fechaCompromiso, fechaCompromisoBtk)) ) {
			result = "Fecha EOM y Beetrack distintas";
		} else {
			result = "";
		}

		return result;
	}

	/**
	 * Metodo que calcula el responsable si cumpe o no con la orden de compra
	 * 
	 * @param String, cumplimiento resumen 
	 * @param String, evoluciona sistema
	 * @param String, evoluciona cd
	 * @param String, primer intento de orden de compra
	 * @param String, cumplimiento de formula
	 * @return String retorna el responsable de la orden de compra
	 */
	private static String obtenerFormulaResponsable(String cumpleResumen, String evalSistema, String evalcd, 
													String evalintento, String cumplFormula) {
		// TODO Auto-generated method stub
		String result = null;
		if (!"CUMPLIMIENTO".equals(cumpleResumen)) {
			if ("No Cumple".equals(evalSistema)) {
				result = "Sistemas";
			} else {
				if ("No Cumple".equals(evalcd)) {
					result = "CD";
				} else {
					if ("Cumple".equals(evalintento)) {
						
						String responsable = buscarResponsable(cumplFormula);
						if (responsable != null) {
							result = responsable;
						} else {
							result = "Cliente";
						}
					} else {
						if (("ATRASADO".equals(cumpleResumen) && "Cumple"
								.equals(evalcd))
								|| ("Cumple".equals(evalcd) && "No Cumple"
										.equals(evalintento))) {
							result = "Transporte";
						} else {
							if ("Sin Información".equals(evalcd)
									&& "No Cumple".equals(evalintento)
									|| ("En Ruta o CT".equals(cumplFormula) || "ATRASADO"
											.equals(cumplFormula))) {
								result = "Sin Información";
							} else {
								
								String responsable = buscarResponsable(cumplFormula);
								if (responsable != null) {
									result = responsable;
								} else {
									result = "Sin información";
								}
							}
						}
					}
				}
			}
		} else {
			result = "-";
		}
		return result;
	}

	/**
	 * Metodo que obtiene el tipo de cumplimiento de la orden de compra
	 * 
	 * @param String, cumplimiento
	 * @return String indica el tipo de cumplimiento de la orden compra
	 */
	private static String obtenerFormulaTipoCumplimiento(String cumple) {
		// TODO Auto-generated method stub
		String result = null;
		if ("1.- CUMPLE".equals(cumple)) {
			result = "CUMPLIMIENTO";
		} else {
			result = "INCUMPLIMIENTO";
		}
		return result;
	}

	/**
	 * Metodo que busca el responsable de la orden de compra
	 * 
	 * @param String, sub estado de orden de compra
	 * @return String indica el reponsable de orden de compra
	*/
	private static String buscarResponsable(String subEstado) {
		String result = null;
		if (subEstado !=null) {
			switch (subEstado) {
	         case "Expectativa":
	        	 result = "Cliente";
	             break;
	         case "Daño Producto":
	        	 result = "Proveedor, CD o Transporte";
	             break;
	         case "Cliente No Está":
	        	 result = "Cliente";
	             break;
	         case "Sin Informacion":
	        	 result = "Falta Información";
	             break;
	         case "Dirección Errónea":
	        	 result = "Cliente";
	             break;
	         case "Producto No Corresponde":
	        	 result = "CD o Transporte";
	             break;
	         case "Motivos Cliente":
	        	 result = "Cliente";
	             break;
	         case "Motivo Transportes":
	        	 result = "Transporte";
	             break;
	         case "En Ruta o CT":
	        	 result = "Transporte";
	             break;   
	         case "NCredito":
	        	 result = "Cliente";
	             break;  
	         case "Nota de Crédito":
	        	 result = "Cliente";
	             break;
	         case "Preparación CD":
	        	 result = "CD";
	             break;  
	         case "En Proceso de Carga":
	        	 result = "CD";
	             break;  
	         case "RechaEXP":
	        	 result = "Cliente";
	             break;
	         case "Error Sistémico":
	        	 result = "Sistemas";
	             break;
	         case "Problema Carga Guía":
	        	 result = "CD o Sistemas";
	             break;    
	         default:
	        	 result = null;	
	     }
		} else {
			result = null;
		}
		return result;
		
	}
	
	/**
	 * Metodo que hace commit en la base de datos
	 * 
	 * @param Connection, conexion a la base de datos
	 * @return si valor de retorno
	*/
	private static void commit(Connection dbconnection,  String sql) {
		logger.info("Inicio commit.");
		PreparedStatement pstmt = null;
		try {
			pstmt = dbconnection.prepareStatement(sql);
			logger.info("registros commit : " + pstmt.executeUpdate());
			
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("[actualizarModelo]Exception:" + e.getMessage());
		}
		finally {
			cerrarTodo(null, pstmt, null);
		}
		logger.info("Fin Acommit.");

	}

	/**
	 * Metodo que ejecuta la eliminacion de registros en una tabla 
	 * 
	 * @param Connection, conexion de las base de datos
	 * @param String, query para la eliminacion  
	 * @return 
	 */
	private static void elimnarCumplimiento(Connection dbconnection,  String sql) {
		logger.info("Inicio elimnar Cumplimiento.");
		PreparedStatement pstmt = null;
		try {
			pstmt = dbconnection.prepareStatement(sql);
			logger.info("registros elimnados Cumplimient : " + pstmt.executeUpdate());
			
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("[elimnarCumplimiento]Exception:" + e.getMessage());
		}
		finally {
			cerrarTodo(null, pstmt, null);
		}
		logger.info("Fin elimnar Cumplimiento.");

	}
	
	/**
	 * Metodo que ejecuta la eliminacion de registros en una tabla 
	 * 
	 * @param Connection, conexion de las base de datos
	 * @param String, query para la eliminacion  
	 * @return 
	 */
	private static void elimnarCumplimientoKpi(Connection dbconnection,  String sql) {
		logger.info("Inicio elimnar Cumplimiento.");
		PreparedStatement pstmt = null;
		try {
			pstmt = dbconnection.prepareStatement(sql);
			logger.info("registros elimnados Cumplimient KPI : " + pstmt.executeUpdate());
			
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("[elimnarCumplimiento]Exception:" + e.getMessage());
		}
		finally {
			cerrarTodo(null, pstmt, null);
		}
		logger.info("Fin elimnar Cumplimiento KPI.");

	}
	
	/**
	 * Metodo que ejecuta la eliminacion de registros en una tabla 
	 * 
	 * @param Connection, conexion de las base de datos
	 * @param String, query para la eliminacion  
	 * @return 
	
	private static void elimnarCumplimientoEstatico(Connection dbconnection,  String sql) {
		logger.info("Inicio elimnar Cumplimiento.");
		PreparedStatement pstmt = null;
		try {
			pstmt = dbconnection.prepareStatement(sql);
			logger.info("registros elimnados Cumplimient Estatico : " + pstmt.executeUpdate());
			
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("[elimnarCumplimiento]Exception:" + e.getMessage());
		}
		finally {
			cerrarTodo(null, pstmt, null);
		}
		logger.info("Fin elimnar Cumplimiento Estatico.");

	}
	*/
	
	
	/**
	 * Metodo que ejecuta la eliminacion de registros en una tabla 
	 * 
	 * @param Connection, conexion de las base de datos
	 * @param String, query para la eliminacion  
	 * @return 
	 */
	/*
	private static void agregarCumplimientoEstatico(Connection dbconnection,  String sql) {
		logger.info("Inicio elimnar Cumplimiento.");
		PreparedStatement pstmt = null;
		try {
			pstmt = dbconnection.prepareStatement(sql);
			logger.info("registros agregados Cumplimientos : " + pstmt.executeUpdate());
			
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("[elimnarCumplimiento]Exception:" + e.getMessage());
		}
		finally {
			cerrarTodo(null, pstmt, null);
		}
		logger.info("Fin elimnar Cumplimiento Estatico.");

	}
*/
	/**
	 * Metodo que crea la conexion a la base de datos txt 
	 *
	 * @return 
	 */
	private static Connection crearConexion() {
		logger.info("Creando conexion a ORACLE.");
		Connection dbconnection = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			dbconnection = DriverManager.getConnection(
					"jdbc:oracle:thin:@172.18.163.15:1521/XE", "txd", "txd");
		}

		catch (Exception e) {
			logger.error("Error de conexion "+ e.getMessage());
		}

		return dbconnection;
	}
	/**
	 * Metodo que crea la conexion a la base de datos kpiweb 
	 *
	 * @return 
	 */
	private static Connection crearConexion2() {
		logger.info("Creando conexion a ORACLE2.");
		Connection dbconnection = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			dbconnection = DriverManager.getConnection(
					"jdbc:oracle:thin:@172.18.163.15:1521/XE", "kpiweb",
					"kpiweb");
			
		}

		catch (Exception e) {
			logger.error("Error de conexion "+ e.getMessage());
		}

		return dbconnection;
	}

	/**
	 * Metodo que cierra todas la conexiones y archivos
	 *
	 * @return 
	 */
	private static void cerrarTodo(Connection cnn, PreparedStatement pstmt,
			BufferedWriter bw) {

		try {
			if (cnn != null) {
				cnn.close();
				cnn = null;
			}
		}

		catch (Exception e) {
			logger.error("Error inesperado "+ e.getMessage());
		}

		try {

			if (pstmt != null) {

				pstmt.close();
				pstmt = null;
			}
		}

		catch (Exception e) {

			logger.error("Error inesperado "+ e.getMessage());
		}
		try {

			if (bw != null) {

				bw.flush();
				bw.close();
				bw = null;
			}
		} catch (Exception e) {

			logger.error("Error inesperado "+ e.getMessage());
		}
	}

	/**
	 * Metodo que entrega fecha actual con formato
	 *
	 * @return 
	*/
	public static String getFechaActual() {
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("yyyyMMdd");
		return formateador.format(ahora);
	}

	/**
	 * Metodo que entrega el dia de la semana
	 * 
	 * @param String, fecha compromiso
	 * @param String, qdirigir el tiempo de transporte  
	 * @return indica el dia de la semana 
	 */
	public static int diaDeLaSemana(String fechaCompromiso, String leadTimeTransport) {

		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
		String strFecha = restarDiasFecha(fechaCompromiso, leadTimeTransport);
		Date fecha = null;
		if (fechaCompromiso != null) {
			try {

				fecha = formatoDelTexto.parse(strFecha);

			} catch (ParseException ex) {

				ex.printStackTrace();
				logger.error("Error funcion diaDeLaSemana fecha"+ ex.getMessage());
			}
		}

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(fecha);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Metodo que resta dias
	 * 
	 * @param String, fecha compromiso
	 * @param String, qdirigir el tiempo de transporte  
	 * @return indica la fecha de resta de dias
	 */
	public static String restarDiasFecha(String fechaCompromiso, String leadTimeTransport) {
		
		String date = null;
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");

		if ("0".equals(leadTimeTransport)) {
			date = "0";
		} else {
			if ("".equals(leadTimeTransport) || leadTimeTransport != null) {
				date = "-" + leadTimeTransport;
			} else {
				date = "0";
			}
		}

		Date fecha = null;
		try {

			fecha = formatoDelTexto.parse(fechaCompromiso);

		} catch (ParseException ex) {

			ex.printStackTrace();
			logger.error("Error funcion restarDiasFecha fecha"+ ex.getMessage());
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha); // Configuramos la fecha que se recibe
		calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(date)); // numero de días a añadir, o restar en caso de días<0
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd");
		return formatoDeFecha.format(calendar.getTime());
	}

	public static Date fechaDesdeString(String strFecha) {
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
		Date fecha = null;

		if (strFecha != null) {
			try {
				fecha = formatoDelTexto.parse(strFecha);
			} catch (java.text.ParseException ex) {
				ex.printStackTrace();
				logger.error("Error funcion fechaDesdeString fecha"+ ex.getMessage());
			}
		} else {
			fecha = null;
		}
		return fecha;

	}

	/**
	 * Metodo que entrega diferencia de dias
	 * 
	 * @param String, fecha inicial
	 * @param String, fecha final  
	 * @return indica el numero de diferencia entre fechas
	 */
	public static int fechasDiferenciaEnDias(Date fechaInicial, Date fechaFinal) {

		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		String fechaInicioString = df.format(fechaInicial);
		try {
			fechaInicial = df.parse(fechaInicioString);
		} catch (ParseException ex) {
			logger.error("Error funcion fechasDiferenciaEnDias fechaInicial"+ ex.getMessage());
		}

		String fechaFinalString = df.format(fechaFinal);
		try {
			fechaFinal = df.parse(fechaFinalString);
		} catch (ParseException ex) {
			logger.error("Error funcion fechasDiferenciaEnDias fechaFinal"+ ex.getMessage());
		}

		long fechaInicialMs = fechaInicial.getTime();
		long fechaFinalMs = fechaFinal.getTime();
		long diferencia = fechaInicialMs - fechaFinalMs;
		double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
		return ((int) dias);
	}

	/**
	 * Metodo que obtiene la fecha mayor
	 * 
	 * @param String, fecha carga requerida
	 * @param String, fecha cierre carga
	 * @return boolean indicando true si se hizo la fecha carga es mayor a la fecha cierre o false sino lo es
	 */
	public static boolean obtenerFechaMayor(String fechaCargaRequerida, String fechaCierreCarga) {
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");

		Date fecha = null;
		Date fecha2 = null;
		try {
			fecha = formatoDelTexto.parse(fechaCargaRequerida);
		} catch (ParseException ex) {
			ex.printStackTrace();
			logger.error("Error funcion obtenerFechaMayor fecha"+ ex.getMessage());
		}
		try {
			fecha2 = formatoDelTexto.parse(fechaCierreCarga);
		} catch (ParseException ex) {
			ex.printStackTrace();
			logger.error("Error funcion obtenerFechaMayor fecha2"+ ex.getMessage());
		}
		return fecha.after(fecha2);
	}

	/**
	 * Metodo que obtiene la fecha menor
	 * 
	 * @param String, fecha primero intento
	 * @param String, fecha compromiso
	 * @return boolean indicando true si la la fecha primero intento es menor a la fecha compromiso o false sino lo es
	*/
	public static boolean obtenerFechaMenor(String fechaPrimerIntento, String fechaCompromisoEom) {
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");

		Date fecha = null;
		Date fecha2 = null;
		try {
			fecha = formatoDelTexto.parse(fechaPrimerIntento);
		} catch (ParseException ex) {
			ex.printStackTrace();
			logger.error("Error funcion obtenerFechaMenor fecha2"+ ex.getMessage());
		}
		try {
			fecha2 = formatoDelTexto.parse(fechaCompromisoEom);
		} catch (ParseException ex) {
			ex.printStackTrace();
			logger.error("Error funcion obtenerFechaMenor fecha2"+ ex.getMessage());
		}
		return fecha2.after(fecha);
	}
	
	/**
	 * Metodo que obtiene la fecha mayor o igual
	 * 
	 * @param String, fecha carga requerida
	 * @param String, fecha cierre carga
	 * @return boolean indicando true si la fecha carga requerida intento es mayor o igual a la fecha cierre carga o false sino lo es
	*/
	public static boolean obtenerFechaMayorIgual(String fechaCargaRequerida, String fechaCierreCarga) {
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");

		Date fecha = null;
		Date fecha2 = null;
		try {
			fecha = formatoDelTexto.parse(fechaCargaRequerida);
		} catch (ParseException ex) {
			ex.printStackTrace();
			logger.error("Error funcion obtenerFechaMayorIgual fecha"+ ex.getMessage());
		}
		try {
			fecha2 = formatoDelTexto.parse(fechaCierreCarga);
		} catch (ParseException ex) {
			ex.printStackTrace();
			logger.error("Error funcion obtenerFechaMayorIgual fecha2"+ ex.getMessage());
		}
		return fecha.after(fecha2) || fecha2.equals(fecha);
	}

	/**
	 * Metodo que obtiene la fecha menor o igual
	 * 
	 * @param String, fecha primer intento
	 * @param String, fecha compromiso
	 * @return boolean indicando true si la la fecha primero intento es mayor o igual a la fecha compromiso carga o false sino lo es
	*/
	public static boolean obtenerFechaMenorIgual(String fechaPrimerIntento, String fechaCompromisoEom) {
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");

		Date fecha = null;
		Date fecha2 = null;
		try {
			fecha = formatoDelTexto.parse(fechaPrimerIntento);
		} catch (ParseException ex) {
			ex.printStackTrace();
			logger.error("Error funcion obtenerFechaMenorIgual fecha2"+ ex.getMessage());
		}
		try {
			fecha2 = formatoDelTexto.parse(fechaCompromisoEom);
		} catch (ParseException ex) {
			ex.printStackTrace();
			logger.error("Error funcion obtenerFechaMenorIgual fecha2"+ ex.getMessage());
		}
		return fecha2.after(fecha) || fecha2.equals(fecha);
	}

	/**
	 * Metodo que obtiene la fecha es igual
	 * 
	 * @param String, fecha numero uno
	 * @param String, fecha numero dos
	 * @return boolean indicando true si la la fecha uno es igual a la fecha dos  o false sino lo es
	*/
	public static boolean obtenerFechaIgual(String fechaUno, String fechaDos) {
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");

		Date fecha = null;
		Date fecha2 = null;
		try {
			fecha = formatoDelTexto.parse(fechaUno);
		} catch (ParseException ex) {
			ex.printStackTrace();
			logger.error("Error funcion obtenerFechaIgual fecha2"+ ex.getMessage());
		}
		try {
			fecha2 = formatoDelTexto.parse(fechaDos);
		} catch (ParseException ex) {
			ex.printStackTrace();
			logger.error("Error funcion obtenerFechaIgual fecha2"+ ex.getMessage());
		}
		return fecha.equals(fecha2);
	}
	
	private static void info(String texto){

		try {

			bw.write(texto+"\n");
			bw.flush();
		}
		catch (Exception e) {

			System.out.println("Exception:" + e.getMessage());
		}
	}
}
