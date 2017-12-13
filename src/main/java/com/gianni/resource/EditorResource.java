package com.gianni.resource;

import com.gianni.entity.ClasseCompiled;
import com.gianni.entity.Result;
import org.codehaus.commons.compiler.CompilerFactoryFactory;
import org.codehaus.commons.compiler.IClassBodyEvaluator;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;

public class EditorResource {

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response executeOnlineCode(ClasseCompiled code) throws Exception {

        IClassBodyEvaluator cbe = CompilerFactoryFactory.getDefaultCompilerFactory().newClassBodyEvaluator();
        cbe.cook(code.getCode());
        Class<?> c = cbe.getClazz();
        Object o = c.newInstance();

        Method[] methods = c.getMethods();

        Result result = new Result();

        for(Method method : methods){
            if(code.getMethodName().equals(method.getName())){

                if("String".compareToIgnoreCase(code.getReturnType())==0){
                    String risultato = (String) method.invoke(o);
                    result.setEsito("OK");
                    result.setRisultato(risultato);
                }

                if("int".compareToIgnoreCase(code.getReturnType()) == 0){
                    int risultato = (Integer) method.invoke(o);
                    result.setEsito("OK");
                    result.setRisultato(String.valueOf(risultato));
                }
            }
        }

        return Response.ok().entity(result).build();
    }
}
