/*
 * Copyright (c) 2012, Niclas Hedhman. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.qi4j.tutorials.hello;

import org.junit.Test;
import org.qi4j.api.value.ValueBuilder;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

// START SNIPPET: step1
public class HelloTest extends AbstractQi4jTest
{
// END SNIPPET: step1

    // START SNIPPET: step2
    @Override
    public void assemble( ModuleAssembly module )
        throws AssemblyException
    {
        module.values( Hello.class );
    }
    // END SNIPPET: step2

    // START SNIPPET: step3
    @Test
    public void givenHelloValueInitializedToHelloWorldWhenCallingSayExpectHelloWorld()
    {
        ValueBuilder<Hello> builder = module.newValueBuilder( Hello.class );
        builder.prototypeFor( Hello.State.class ).phrase().set( "Hello" );
        builder.prototypeFor( Hello.State.class ).name().set( "World" );
        Hello underTest = builder.newInstance();
        String result = underTest.say();
        assertThat( result, equalTo( "Hello World" ) );
    }
    // END SNIPPET: step3

// START SNIPPET: step1
}
// END SNIPPET: step1
