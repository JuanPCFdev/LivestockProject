package com.jpdev.livestockproject.data.tips

class TipsProvider {

    fun getList():List<Tips>{
        var tipsList = mutableListOf<Tips>()

        val tip1 = Tips("Elección de Razas Adecuadas",
                "Opta por razas de ganado bovino que se adapten al clima y las condiciones de tu área, " +
                        "considerando factores como resistencia a enfermedades locales y calidad de carne o leche.")

        val tip2 = Tips("Planificación del Espacio",
                "Planifica la distribución de pastizales, corrales y áreas de descanso para optimizar la " +
                        "alimentación, manejo y bienestar del ganado.")

        val tip3 = Tips("Manejo Nutricional",
                "Consulta con un nutricionista animal para garantizar que el ganado reciba los nutrientes " +
                        "necesarios, considerando la edad, peso y propósito (carne o leche).")

        val tip4 = Tips("Infraestructura de Agua",
                "Coloca bebederos estratégicamente y monitorea la calidad del agua para mantener la salud del ganado.")

        val tip5 = Tips("Control Sanitario",
                "Implementa vacunaciones, desparasitaciones y revisiones periódicas para prevenir enfermedades " +
                        "y mantener la salud del rebaño.")

        val tip6 = Tips("Registro y Documentación",
                "Registra información sobre nacimientos, vacunas, tratamientos y producción para facilitar la " +
                        "gestión y tomar decisiones informadas.")

        val tip7 = Tips("Manejo de Desechos",
                "Establece sistemas adecuados para el manejo de estiércol y residuos, minimizando impactos ambientales " +
                        "y cumpliendo normativas locales.")

        val tip8 = Tips("Compra de Ganado",
                "Asegúrate de comprar ganado de proveedores con historial sanitario comprobado para evitar la " +
                        "introducción de enfermedades en tu rebaño.")

        val tip9 = Tips("Capacitación Continua",
                "Participa en talleres, cursos y eventos para aprender nuevas técnicas, tecnologías y avances en la ganadería bovina.")

        val tip10 = Tips("Monitoreo Constante",
                "Un seguimiento constante permite ajustar la alimentación y detectar posibles problemas de salud a tiempo.")

        val tip11 = Tips("Emergencias",
                "Anticípate a posibles desastres naturales, enfermedades repentinas o escapes de animales, teniendo " +
                        "un plan claro y recursos necesarios.")

        val tip12 = Tips("Gestión de Personal",
                "Una mano de obra bien capacitada y motivada contribuye al manejo eficiente y al bienestar del ganado.")

        val tip13 = Tips("Cuidado de Pastizales",
                "Rotaciona pastizales, evita la sobreexplotación y promueve la regeneración para mantener pasturas saludables.")

        val tip14 = Tips("Evaluación Económica",
                "Evalúa el rendimiento financiero de tu operación para tomar decisiones informadas y optimizar la rentabilidad.")

        val tip15 = Tips("Selección Genética",
                "Elige reproductores con características deseables para mejorar la calidad genética y la productividad de tu ganado.")

        val tip16 = Tips("Programa de Reproducción",
                "Maximiza la eficiencia reproductiva planificando los momentos ideales para la reproducción y partos.")

        val tip17 = Tips("Registros de Producción",
                "Registra datos relacionados con la producción de carne o leche para identificar áreas de mejora y optimizar la eficiencia.")

        val tip18 = Tips("Normativas Locales",
                "Cumple con los requisitos legales y ambientales para evitar problemas legales y garantizar la sostenibilidad de tu operación.")

        val tip19 = Tips("Diversificación de Ingresos",
                "Considera actividades complementarias, como turismo rural o venta directa de productos, para diversificar " +
                        "tus fuentes de ingresos.")

        val tip20 = Tips("Contactos y Asesoramiento Profesional",
                "Mantén contactos con otros ganaderos, profesionales del sector y asesores especializados para obtener " +
                        "orientación y compartir experiencias.")

        tipsList.add(tip1)
        tipsList.add(tip2)
        tipsList.add(tip3)
        tipsList.add(tip4)
        tipsList.add(tip5)
        tipsList.add(tip6)
        tipsList.add(tip7)
        tipsList.add(tip8)
        tipsList.add(tip9)
        tipsList.add(tip10)
        tipsList.add(tip11)
        tipsList.add(tip12)
        tipsList.add(tip13)
        tipsList.add(tip14)
        tipsList.add(tip15)
        tipsList.add(tip16)
        tipsList.add(tip17)
        tipsList.add(tip18)
        tipsList.add(tip19)
        tipsList.add(tip20)

        return tipsList
    }

}