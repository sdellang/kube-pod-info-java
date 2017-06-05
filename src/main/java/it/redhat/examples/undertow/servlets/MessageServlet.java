package it.redhat.examples.undertow.servlets;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class MessageServlet extends HttpServlet {

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

        KubernetesClient client = new DefaultKubernetesClient();
        PodList pl = client.pods().list();

        PrintWriter writer = resp.getWriter();

        for(Pod p : pl.getItems()) {
            String podIP = p.getStatus().getPodIP();
            String podName = p.getMetadata().getName();
            writer.write(podName+" "+podIP+"\n");
        }



        writer.close();
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
