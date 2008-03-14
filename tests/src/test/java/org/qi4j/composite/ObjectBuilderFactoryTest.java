/*
 * Copyright (c) 2007, Rickard Öberg. All Rights Reserved.
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

package org.qi4j.composite;

import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.bootstrap.SingletonAssembler;
import org.qi4j.bootstrap.AssemblyException;
import org.junit.Test;

/**
 * Unit tests for ObjectBuilderFactory.
 */
public class ObjectBuilderFactoryTest
{

    /**
     * Tests that an object builder cannot be created for an unregistered object.
     *
     * @throws Exception expected
     */
    @Test( expected = Exception.class )
    public void newBuilderForUnregisteredObject()
        throws Exception
    {
        SingletonAssembler assembler = new SingletonAssembler()
        {
            public void assemble( ModuleAssembly module ) throws AssemblyException
            {
            }
        };
        assembler.getObjectBuilderFactory().newObjectBuilder( AnyObject.class );
    }

    /**
     * Tests that an object builder can be created for an registered object.
     */
    @Test
    public void newBuilderForRegisteredObject()
    {
        SingletonAssembler assembler = new SingletonAssembler()
        {
            public void assemble( ModuleAssembly module ) throws AssemblyException
            {
                module.addObjects( AnyObject.class );
            }
        };
        assembler.getObjectBuilderFactory().newObjectBuilder( AnyObject.class );
    }

    public static final class AnyObject
    {
    }

}
