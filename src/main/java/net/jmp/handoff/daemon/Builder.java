package net.jmp.handoff.daemon;

/*
 * (#)Builder.java  0.8.0   05/10/2024
 *
 * @author    Jonathan Parker
 * @version   0.8.0
 * @since     0.8.0
 *
 * MIT License
 *
 * Copyright (c) 2024 Jonathan M. Parker
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * A generic object builder.
 *
 * @param   <T> The type of class instance to build
 */
final class Builder<T> {
    /** The supplier for the new class instance. */
    private final Supplier<T> supplier;

    /**
     * The constructor.
     *
     * @param   supplier    java.util.function.Supplier
     */
    private Builder(final Supplier<T> supplier) {
        super();

        this.supplier = supplier;
    }

    /**
     * Return an instance of the builder class.
     *
     * @param   supplier    java.util.function.Supplier
     * @return              net.jmp.handoff.daemon.Builder&lt;T&gt;
     * @param   <T>         The type of class instance to build
     */
    static <T> Builder<T> of(final Supplier<T> supplier) {
        return new Builder<>(supplier);
    }

    /**
     * Apply object properties to the object being built.
     *
     * @param   consumer    java.util.function.BiConsumer
     * @param   value       P
     * @return              net.jmp.handoff.daemon.Builder&lt;T&gt;
     * @param   <P>         The type of value being applied to the object
     */
    <P> Builder<T> with(final BiConsumer<T, P> consumer, final P value) {
        return new Builder<>(() -> {
            final T object = this.supplier.get();

            consumer.accept(object, value);

            return object;
        });
    }

    /**
     * Return the fully built class instance.
     *
     * @return  T
     */
    T build() {
        return this.supplier.get();
    }
}
