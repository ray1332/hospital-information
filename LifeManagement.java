/**
 * 医疗信息管理系统 - 生命体征监测模块
 * 功能：录入患者生命体征数据、保存、报警、显示历史记录
 * 作者：项目开发组
 * 日期：2025-04-10
 */

import java.util.*;

class VitalSign {
    private String patientId;
    private double temperature;
    private int heartRate;
    private int bloodPressure;
    private int respirationRate;
    private Date recordTime;

    public VitalSign(String patientId, double temperature, int heartRate, int bloodPressure, int respirationRate) {
        this.patientId = patientId;
        this.temperature = temperature;
        this.heartRate = heartRate;
        this.bloodPressure = bloodPressure;
        this.respirationRate = respirationRate;
        this.recordTime = new Date();
    }

    public String getPatientId() {
        return patientId;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public int getBloodPressure() {
        return bloodPressure;
    }

    public int getRespirationRate() {
        return respirationRate;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    @Override
    public String toString() {
        return "患者ID: " + patientId +
                ", 体温: " + temperature +
                ", 心率: " + heartRate +
                ", 血压: " + bloodPressure +
                ", 呼吸频率: " + respirationRate +
                ", 记录时间: " + recordTime;
    }
}

public class VitalSignMonitor {
    private List<VitalSign> records = new ArrayList<>();

    // 录入生命体征数据
    public void addRecord(VitalSign sign) {
        records.add(sign);
        checkAlarm(sign);
    }

    // 检查报警
    private void checkAlarm(VitalSign sign) {
        if (sign.getTemperature() > 38.5) {
            System.out.println("[报警] 患者 " + sign.getPatientId() + " 体温过高: " + sign.getTemperature() + "°C");
        }
        if (sign.getHeartRate() < 50 || sign.getHeartRate() > 120) {
            System.out.println("[报警] 患者 " + sign.getPatientId() + " 心率异常: " + sign.getHeartRate() + " bpm");
        }
        if (sign.getBloodPressure() < 80 || sign.getBloodPressure() > 180) {
            System.out.println("[报警] 患者 " + sign.getPatientId() + " 血压异常: " + sign.getBloodPressure() + " mmHg");
        }
        if (sign.getRespirationRate() < 12 || sign.getRespirationRate() > 30) {
            System.out.println("[报警] 患者 " + sign.getPatientId() + " 呼吸频率异常: " + sign.getRespirationRate() + " 次/分");
        }
    }

    // 查询患者生命体征历史记录
    public void showRecordsByPatient(String patientId) {
        System.out.println("患者 " + patientId + " 的生命体征历史记录:");
        for (VitalSign sign : records) {
            if (sign.getPatientId().equals(patientId)) {
                System.out.println(sign);
            }
        }
    }

    public static void main(String[] args) {
        VitalSignMonitor monitor = new VitalSignMonitor();

        // 模拟录入数据
        monitor.addRecord(new VitalSign("A001", 37.5, 80, 120, 18));
        monitor.addRecord(new VitalSign("A001", 38.8, 130, 115, 22));
        monitor.addRecord(new VitalSign("A002", 36.9, 70, 90, 16));
        monitor.addRecord(new VitalSign("A001", 39.0, 140, 160, 35));

        // 查询患者A001的历史记录
        monitor.showRecordsByPatient("A001");
    }
}
