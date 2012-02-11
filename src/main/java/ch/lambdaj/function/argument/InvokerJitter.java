package ch.lambdaj.function.argument;

import net.sf.cglib.asm.ClassWriter;
import net.sf.cglib.asm.Label;
import net.sf.cglib.asm.MethodVisitor;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import static net.sf.cglib.asm.Opcodes.*;

public class InvokerJitter {

    private static final AtomicInteger jitCounter = new AtomicInteger(1);

    private final InternalClassLoader classLoader;
    private final InvocationSequence invocationSequence;

    InvokerJitter(Object invokedObject, InvocationSequence invocationSequence) {
        this(invokedObject.getClass().getClassLoader(), invocationSequence);
    }

    InvokerJitter(ClassLoader classLoader, InvocationSequence invocationSequence) {
        this.classLoader = new InternalClassLoader(classLoader);;
        this.invocationSequence = invocationSequence;
    }

    Invoker jitInvoker() {
        try {
            int id = jitCounter.getAndIncrement();
            Class<?> clazz = classLoader.defineClass("ch.lambdaj.function.argument.Invoker_" + id, generateBytecode(id));
            return (Invoker)clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] generateBytecode(int id) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS + ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, "ch/lambdaj/function/argument/Invoker_" + id, null, "java/lang/Object", new String[]{"ch/lambdaj/function/argument/Invoker"});
        jitEmptyConstructor(cw);
        jitInvocationMethod(cw);
        cw.visitEnd();
        return cw.toByteArray();
    }

    private void jitEmptyConstructor(ClassWriter cw) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private void jitInvocationMethod(ClassWriter cw) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "invokeOn", "(Ljava/lang/Object;)Ljava/lang/Object;", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 1);
        jitInvocationSequence(mv);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private void jitInvocationSequence(MethodVisitor mv) {
        mv.visitVarInsn(ALOAD, 1);
        mv.visitTypeInsn(CHECKCAST, getClassName(invocationSequence.getRootInvokedClass()));
        jitInvocation(mv, invocationSequence.lastInvocation);
    }

    private void jitInvocation(MethodVisitor mv, Invocation invocation) {
        if (invocation.previousInvocation != null) jitInvocation(mv, invocation.previousInvocation);
        mv.visitVarInsn(ASTORE, 2);
        Label nonNull = new Label();
        mv.visitVarInsn(ALOAD, 2);
        mv.visitJumpInsn(IFNONNULL, nonNull);
        mv.visitInsn(ACONST_NULL);
        mv.visitInsn(ARETURN);
        mv.visitLabel(nonNull);
        mv.visitVarInsn(ALOAD, 2);
        Method method = invocation.getInvokedMethod();
        if (method.getDeclaringClass().isInterface()) {
            mv.visitMethodInsn(INVOKEINTERFACE, getClassName(method.getDeclaringClass()), method.getName(), "()" + getInternalName(method.getReturnType()));
        } else {
            mv.visitMethodInsn(INVOKEVIRTUAL, getClassName(method.getDeclaringClass()), method.getName(), "()" + getInternalName(method.getReturnType()));
        }
        primitiveToObject(mv, method.getReturnType());
    }

    private String getClassName(Class<?> clazz) {
        return clazz.getName().replace('.', '/');
    }

    private String getInternalName(Class<?> clazz) {
        if (!clazz.isPrimitive()) return "L" + getClassName(clazz) + ";";
        if (clazz == int.class) return "I";
        if (clazz == long.class) return "J";
        if (clazz == double.class) return "D";
        if (clazz == float.class) return "F";
        if (clazz == boolean.class) return "Z";
        if (clazz == char.class) return "C";
        if (clazz == short.class) return "S";
        return "B";
    }

    private void primitiveToObject(MethodVisitor mv, Class<?> clazz) {
        if (!clazz.isPrimitive()) return;
        if (clazz == int.class) mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
        else if (clazz == long.class) mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
        else if (clazz == double.class) mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
        else if (clazz == float.class) mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
        else if (clazz == boolean.class) mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
        else if (clazz == char.class) mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
        else if (clazz == short.class) mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
        else if (clazz == byte.class) mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
    }

    private static class InternalClassLoader extends ClassLoader {

        InternalClassLoader(ClassLoader classLoader) {
            super(classLoader);
        }

        Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }
}
