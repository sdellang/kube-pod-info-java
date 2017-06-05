# Fabric8 Openshift/Kubernetes Java Client Example

This example is based on the [Undertow Servlet Quinckstart](https://github.com/jboss-openshift/openshift-quickstarts/tree/master/undertow-servlet) and uses [Fabric8 Kubernetes Client](https://github.com/fabric8io/kubernetes-client)

To run the example locally you will need to install [minishift](https://github.com/minishift/minishift) or [cdk3](https://developers.redhat.com/products/cdk/overview/).

***This example is based on Openshift, but the code is fully compatible with Kubernetes***

First of all create a new project:

`oc new-project javaclienttest`

We will use the java openjdk8 s2i image. To do this you need to create an imagestream.
[Here](https://gist.githubusercontent.com/tqvarnst/3ca512b01b7b7c1a1da0532939350e23/raw/3869a54c7dd960965f0e66907cdc3eba6d160cad/openjdk-s2i-imagestream.json) you can find the definition, just copy and paste it in a file.

Then run:

`oc login -u system:admin`

`oc create -f *your_imagestream_file* -n openshift`

Now you are ready to deploy the example:

`oc login -u developer` (password is: developer)

`oc project javaclienttest`

`oc new-app redhat-openjdk18-openshift~https://github.com/sdellang/kube-pod-info-java`

It's almost done. You will only need to create a serviceaccount with the right permissions to see cluster resources:

`oc create serviceaccount java-view-sa`

`oc policy add-role-to-user view system:serviceaccount:javatest:java-view-sa`

Then edit the deploymentconfig

`oc edit dc javatest`

adding the service account name that will run this pod.

```
...
containers:
      - image: 172.30.1.1:5000/javatest/javatest@sha256:51f505af96ba2ea0fad5a18050ce194ef8a63b336871cb0b9e913bf2550241f1
        imagePullPolicy: Always
        name: javatest
        ports:
        - containerPort: 8080
          protocol: TCP
        - containerPort: 8443
          protocol: TCP
        - containerPort: 8778
          protocol: TCP
        resources: {}
        terminationMessagePath: /dev/termination-log
      dnsPolicy: ClusterFirst
      restartPolicy: Always
      securityContext: {}
      serviceAccount: java-view-sa
      serviceAccountName: java-view-sa
      terminationGracePeriodSeconds: 30
```
There you go. You can now test your application by pasting the route url in your browser.

Something like:

http://javatest-javatest.192.168.64.2.nip.io/

You will see the names of the pods in your project and their ips.

## Contributing

Feel free to contribute and extend this example by forking the project and sending PRs.
