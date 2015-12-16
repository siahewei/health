package com.healthcare.common.db;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by jacky on 15/10/5.
 */
public class TestDaoGenerator {
    public static void main(String[] agrs){
       /* Schema schema = new Schema(Constants.DB_VERSION, "com.healthcare.modules.modle");
        addkinetic(schema);
        addlocus(schema);
        addDataEntity(schema);
        addSteps(schema);
        try {
            new DaoGenerator().generateAll(schema,"/Users/jacky/projs/wearapp/healthcare/app/src/main/java/");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    private static void addlocus(Schema schema) {
        Entity note = schema.addEntity("locus");
        note.addIdProperty();
        note.addDoubleProperty("longtitude");
        note.addDoubleProperty("lattitude");
        note.addFloatProperty("accuracy");
        note.addDateProperty("date");
        note.addStringProperty("userName");
    }

    private static void addkinetic(Schema schema) {
        Entity note = schema.addEntity("kinetics");
        note.addIdProperty();
        note.addFloatProperty("anglex");
        note.addFloatProperty("angley");
        note.addFloatProperty("agnlez");
        note.addFloatProperty("accelx");
        note.addFloatProperty("accely");
        note.addFloatProperty("accelz");
        note.addFloatProperty("gyrox");
        note.addFloatProperty("gyroy");
        note.addFloatProperty("gyroz");
    }

    private static void addDataEntity(Schema schema){
        Entity dataEntity = schema.addEntity("dataentity");
        dataEntity.addLongProperty("timeStamp");
        dataEntity.addFloatProperty("x");
        dataEntity.addFloatProperty("y");
        dataEntity.addFloatProperty("z");
        dataEntity.addIntProperty("type");
        dataEntity.addIntProperty("accuracy");
        dataEntity.addStringProperty("tag");
    }

    private static void addSteps(Schema schema){
        Entity stepEntity = schema.addEntity("stepbean");
        stepEntity.addStringProperty("day");
        stepEntity.addLongProperty("bengin");
        stepEntity.addLongProperty("end");
        stepEntity.addIntProperty("stepcount");
        stepEntity.addIntProperty("source");
    }
}
