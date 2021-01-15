package com.example.mybaseproject.bind

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.example.mybaseproject.utils.extensions.setSafeOnClickListener
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.ArrayList
import java.util.Arrays

/**
 * Created by LeHieu on 8/18/2017.
 */

class ParseViewer {

    fun bind(activity: Activity) {

        val fields = Arrays.asList(*activity.javaClass.declaredFields)
        val methods = ArrayList(Arrays.asList(*activity.javaClass.declaredMethods))
        if (activity.javaClass.superclass != Activity::class.java) {
            val fieldParents = Arrays.asList(*activity.javaClass.superclass!!.declaredFields)
            val methodParents = Arrays.asList(*activity.javaClass.superclass!!.declaredMethods)
            setToFields(activity, fieldParents, methods)
            setToMethods(activity, methodParents)
        }

        setToFields(activity, fields, methods)
        setToMethods(activity, methods)

    }

    private fun setToMethods(activity: Activity, methods: List<Method>) {
        try {
            for (method in methods) {
                method.isAccessible = true
                val anno = method.getAnnotation(Tap::class.java)
                if (anno != null) {
                    for (id in anno.value) {
                        val v = activity.findViewById<View>(id)
                        onViewTap(activity, method, v)
                    }
                    continue
                }
                val done = method.getAnnotation(Done::class.java)
                if (done != null) {
                    val v = activity.findViewById<View>(done.value)
                    if (v is EditText)
                        onViewDone(activity, method, v)
                }
            }
        } catch (e: Exception) {
            Log.w("EX", e)
        }

    }

    private fun setToFields(
            activity: Activity,
            fields: List<Field>?,
            methods: MutableList<Method>
    ) {
        try {
            if (fields != null)
                for (f in fields) {
                    f.isAccessible = true
                    val anno = f.getAnnotation(FindViewer::class.java) ?: continue
                    try {
                        val v = activity.findViewById<View>(anno.value)
                        f.set(activity, f.type.cast(v))
                        clearTag(methods, activity, v, anno.value)
                    } catch (e: Exception) {
                        Log.w("EX", e)
                    }

                }
        } catch (e: Exception) {
            Log.w("EX", e)
        }

    }

    fun bind(target: Any, view: View) {
        val fields = Arrays.asList(*target.javaClass.declaredFields)
        val methods = ArrayList(Arrays.asList(*target.javaClass.declaredMethods))
        for (f in fields) {
            f.isAccessible = true
            val anno = f.getAnnotation(FindViewer::class.java) ?: continue
            try {
                val v = view.findViewById<View>(anno.value)
                f.set(target, f.type.cast(v))
                clearTag(methods, target, v, anno.value)
            } catch (e: Exception) {
                Log.w("EX", e)
            }

        }
        for (method in methods) {
            val anno = method.getAnnotation(Tap::class.java)
            if (anno != null) {
                for (id in anno.value) {
                    val v = view.findViewById<View>(id)
                    onViewTap(target, method, v)
                }
            }
            val done = method.getAnnotation(Done::class.java)
            if (done != null) {
                val v = view.findViewById<View>(done.value)
                if (v is EditText)
                    onViewDone(target, method, v)
            }
        }
    }

    private fun clearTag(methods: MutableList<Method>, target: Any, v: View?, id: Int) {
        var method = removeTapTag(methods, id)
        if (method != null) {
            val anno = method.getAnnotation(Tap::class.java)
            if (anno != null && anno.value.size > 1) {
                //skip multi ids
            } else {
                onViewTap(target, method, v)
                if (methods.contains(method))
                    methods.remove(method)
            }

        }
        if (method != null) {
            val done = method.getAnnotation(Done::class.java)
            if (done != null && v is EditText)
                onViewDone(target, method, v)
        } else {
            method = removeDoneTag(methods, id)
            if (method != null) {
                if (v is EditText)
                    onViewDone(target, method, v)
            }
        }
    }

    private fun removeDoneTag(methods: List<Method>, id: Int): Method? {
        for (method in methods) {
            val done = method.getAnnotation(Done::class.java)
            if (done != null) {
                if (id == done.value)
                    return method
            }
        }
        return null
    }

    private fun onViewDone(target: Any, method: Method, v: EditText) {
        v.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                method.isAccessible = true
                try {
                    val types = method.genericParameterTypes
                    if (types != null && types.size > 0) {
                        method.invoke(target, v)
                    } else
                        method.invoke(target)
                } catch (e: Exception) {
                    Log.w("EX", e)
                }

            }
            false
        }
    }

    private fun onViewTap(target: Any, method: Method, v: View?) {
        try {
            v?.setSafeOnClickListener { v ->
                method.isAccessible = true
                try {
                    val types = method.genericParameterTypes
                    if (types != null && types.size > 0) {
                        method.invoke(target, v)
                    } else
                        method.invoke(target)
                } catch (e: Exception) {
                    Log.w("EX", e)
                }
            }
        } catch (e: Exception) {
            Log.w("EX", e)
        }

    }

    private fun onViewTap(activity: Activity, method: Method, v: View?) {
        try {
            v?.setSafeOnClickListener { v1 ->
                method.isAccessible = true
                try {
                    val types = method.genericParameterTypes
                    if (types != null && types.size > 0) {
                        method.invoke(activity, v1)
                    } else
                        method.invoke(activity)
                } catch (e: Exception) {
                    Log.w("EX", e)
                }
            }
        } catch (e: Exception) {
            Log.w("EX", e)
        }

    }

    private fun removeTapTag(methods: List<Method>, value: Int): Method? {
        for (method in methods) {
            method.isAccessible = true

            val anno = method.getAnnotation(Tap::class.java)
            if (anno != null) {
                for (id in anno.value) {
                    if (id == value)
                        return method
                }
            }
        }
        return null
    }

    private fun findAnnotation(declaredAnnotations: Array<Annotation>): FindViewer? {
        val var3 = declaredAnnotations.size

        for (var4 in 0 until var3) {
            val anno = declaredAnnotations[var4]
            if (anno is FindViewer) {
                return anno
            }
        }

        return null
    }

    companion object {
        private var instance: ParseViewer? = null
        @JvmStatic
        fun getInstance(): ParseViewer {
            if (instance == null)
                instance = ParseViewer()
            return instance!!
        }
    }


}
