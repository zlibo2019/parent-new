package com.weds.drools.util;

import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;

import java.io.IOException;


/**
 * 动态生成kjar
 */
public class DroolsUtils {

    /**
     * 创建默认的kbase和stateful的kiesession
     *
     * @param ks
     * @param isdefault
     * @return
     */
    private static KieFileSystem createKieFileSystemWithKProject(KieServices ks, boolean isdefault) {
        KieModuleModel kproj = ks.newKieModuleModel();
        KieBaseModel kieBaseModel1 = kproj.newKieBaseModel("KBase").setDefault(isdefault)
                .setEqualsBehavior(EqualityBehaviorOption.EQUALITY)
                .setEventProcessingMode(EventProcessingOption.STREAM);
        // Configure the KieSession.
        kieBaseModel1.newKieSessionModel("KSession").setDefault(isdefault)
                .setType(KieSessionModel.KieSessionType.STATEFUL);
        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.writeKModuleXML(kproj.toXML());
        return kfs;
    }

    /**
     * 创建kjar的pom
     *
     * @param releaseId
     * @param dependencies
     * @return
     */
    private static String getPom(ReleaseId releaseId, ReleaseId... dependencies) {
        StringBuilder pom = new StringBuilder();
        pom.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        pom.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n");
        pom.append("         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
        pom.append("         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n");
        pom.append("    <modelVersion>4.0.0</modelVersion>\n");
        pom.append("    <groupId>").append(releaseId.getGroupId()).append("</groupId>\n");
        pom.append("    <artifactId>").append(releaseId.getArtifactId()).append("</artifactId>\n");
        pom.append("    <version>").append(releaseId.getVersion()).append("</version>\n");
        if (dependencies != null && dependencies.length > 0) {
            pom.append("<dependencies>\n");
            for (ReleaseId dep : dependencies) {
                pom.append("<dependency>\n");
                pom.append("    <groupId>").append(dep.getGroupId()).append("</groupId>\n");
                pom.append("    <artifactId>").append(dep.getArtifactId()).append("</artifactId>\n");
                pom.append("    <version>").append(dep.getVersion()).append("</version>\n");
                pom.append("</dependency>\n");
            }
            pom.append("</dependencies>\n");
        }
        pom.append("</project>");
        return pom.toString();
    }

    /**
     * 初始化一个kjar：把原有的drl包含进新建的kjar中
     *
     * @param ks
     * @param releaseId
     * @return
     * @throws IOException
     */
    public static InternalKieModule initKieJar(KieServices ks, ReleaseId releaseId) throws IOException {
        KieFileSystem kfs = createKieFileSystemWithKProject(ks, true);
        kfs.writePomXML(getPom(releaseId));
        KieBuilder kieBuilder = ks.newKieBuilder(kfs);
        if (!kieBuilder.buildAll().getResults().getMessages().isEmpty()) {
            throw new IllegalStateException("Error creating KieBuilder.");
        }
        return (InternalKieModule) kieBuilder.getKieModule();
    }

    /**
     * @param ks
     * @param releaseId
     * @param droolsResource
     * @return
     */
    public static InternalKieModule createKieJar(KieServices ks, ReleaseId releaseId, DroolsResource droolsResource) {
        KieFileSystem kfs = createKieFileSystemWithKProject(ks, true);
        kfs.writePomXML(getPom(releaseId));
        kfs.write("src/main/resources/" + droolsResource.getTargetResourceName(), droolsResource.getResource());

        KieBuilder kieBuilder = ks.newKieBuilder(kfs);
        if (!kieBuilder.getResults().getMessages().isEmpty()) {
            throw new IllegalStateException("Error creating KieBuilder. errorMsg:" + kieBuilder.getResults().getMessages());
        }
        return (InternalKieModule) kieBuilder.getKieModule();
    }
}
